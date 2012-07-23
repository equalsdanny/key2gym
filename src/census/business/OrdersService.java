/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package census.business;

import census.business.api.BusinessException;
import census.business.api.ValidationException;
import census.business.api.SecurityException;
import census.business.dto.OrderDTO;
import census.business.dto.ItemDTO;
import census.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.NoResultException;
import org.joda.time.DateMidnight;

/**
 *
 * @author daniel
 * 
 * A financial activity is open if either one of the following statements is
 * true:
 * <ul>
 * 
 * <li> it's associated with an attendance and the attendance is open.
 * <li> it's associated with a client and was issued today.
 * <li> it's not associated with anything and was issued today.
 * 
 * </ul>
 */
public class OrdersService extends BusinessService {

    /*
     * Singleton instance
     */
    private static OrdersService instance;
    
    /**
     * Finds a financial activity by the client and the date. If it does not
     * exist, and createIfDoesNotExist is true, a financial activity will be created. 
     * Note that a transaction is needed to create a financial activity in this case.
     *
     * @param clientId the ID of the client
     * @param date the date to look up
     * @param createIfDoesNotExist if true, the financial activity will be created,
     * if none is found
     * @throws NullPointerException if any of the arguments is null
     * @throws IllegalStateException if a new financial activity is requested, 
     * but the transaction is not active; if no session is open.
     * @throws ValidationException if the client's ID provided is invalid
     * @return the ID of the financial activity, or null, if none was found
     * and a new one was not requested.
     */
    public Short findByClientIdAndDate(Short clientId, DateMidnight date, Boolean createIfDoesNotExist)
            throws ValidationException {
        
        assertSessionActive();
        
        if(clientId == null) {
            throw new NullPointerException("The clientId is null."); //NOI18N
        }
        
        if(date == null) {
            throw new NullPointerException("The date is null."); //NOI18N
        }

        if (createIfDoesNotExist == null) {
            throw new NullPointerException("The createIfDoesNotExist is null."); //NOI18N
        }

        Client client;
        OrderEntity order;

        /*
         * Finds the client.
         */
        client = entityManager.find(Client.class, clientId);

        if (client == null) {
            throw new ValidationException(bundle.getString("ClientIDInvalid"));
        }

        /*
         * Finds a financial activity associtead with the client and issued
         * today.
         */
        try {
            order = (OrderEntity) entityManager
                    .createNamedQuery("OrderEntity.findByClientAndDateRecorded") //NOI18N
                    .setParameter("client", client) //NOI18N
                    .setParameter("dateRecorded", date.toDate()) //NOI18N
                    .setMaxResults(1)
                    .getSingleResult();
            return order.getId();
        } catch (NoResultException ex) {
            /*
             * If none is found but a new one was requested, creates one.
             */            
            if (createIfDoesNotExist) {
                assertTransactionActive();

                order = new OrderEntity();
                order.setClient(client);
                order.setDate(date.toDate());
                order.setPayment(BigDecimal.ZERO);
                order.setId(getNextId());

                // TODO: note change
                entityManager.persist(order);
                entityManager.flush();

                return order.getId();
            }
        }

        return null;
    }

    /**
     * Finds the today's financial activity for the client. If it does not exist,
     * but a valid card was provided, and createIfDoesNotExist is true, a
     * financial activity will be created. Note that the transaction has to be 
     * active to create a financial activity.
     *
     * @param cardId the card of the client
     * @throws NullPointerException if the card or createIfDoesNotExist is null
     * @throws IllegalStateException if a new financial activity is required, 
     * but the transaction is not active; if the session is not active
     * @throws ValidationException if the card is invalid
     * @return the ID of the financial activity, or null, if none was found
     * and a new one was not requested.
     */
    public Short findCurrentForClientByCard(Integer card, Boolean createIfDoesNotExist)
            throws IllegalArgumentException, IllegalStateException, ValidationException {
        assertSessionActive();
        
        if(card == null) {
            throw new NullPointerException("The card is null."); //NOI18N
        }

        if (createIfDoesNotExist == null) {
            throw new NullPointerException("The createIfDoesNotExist is null"); //NOI18N
        }

        Client client;
        OrderEntity order;

        try {
            client = (Client) entityManager.createNamedQuery("Client.findByCard") //NOI18N
                    .setParameter("card", card) //NOI18N
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new ValidationException(bundle.getString("ClientCardInvalid"));
        }

        try {
            order = (OrderEntity) entityManager
                    .createNamedQuery("OrderEntity.findByClientAndDateRecorded") //NOI18N
                    .setParameter("client", client) //NOI18N
                    .setParameter("dateRecorded", getToday()) //NOI18N
                    .setMaxResults(1)
                    .getSingleResult();
            return order.getId();
        } catch (NoResultException ex) {
            /*
             * If none is found but a new one was requested, create one.
             */
            if (createIfDoesNotExist) {
                assertTransactionActive();

                order = new OrderEntity();
                order.setClient(client);
                order.setDate(getToday());
                order.setPayment(BigDecimal.ZERO);
                order.setId(getNextId());

                // TODO: note change
                entityManager.persist(order);
                entityManager.flush();

                return order.getId();
            }
        }
        return null;
    }

    /**
     * Finds the financial activity associated with the attendance.
     * 
     * <ul>
     * 
     * <li> The attendance must be anonymous.
     * 
     * </ul>
     *
     * @param attendanceId the attendance's ID
     * @throws NullPointerException if the attendance's ID is null
     * @throws IllegalStateException if the session is not active
     * @throws ValidationException if the attendance's ID is invalid
     * @throws BusinessException if current business rules resrict this operation
     * @return the ID of the financial activity or null, if none was found
     * and a new one was not requested.
     */
    public Short findForAttendanceById(Short attendanceId)
            throws IllegalArgumentException, IllegalStateException, ValidationException, BusinessException {

        assertSessionActive();
        
        /*
         * Arguments validation.
         */
        if (attendanceId == null) {
            throw new NullPointerException("The attendanceId is null."); //NOI18N
        }

        Attendance attendance = null;
        OrderEntity order = null;

        attendance = (Attendance) entityManager.find(Attendance.class, attendanceId);

        if (attendance == null) {
            throw new ValidationException(bundle.getString("AttendanceIDInvalid"));
        }
        
        if(attendance.getClient() != null) {
            throw new BusinessException(bundle.getString("AttendanceMustBeAnonymous"));
        }

        try {
            order = (OrderEntity) entityManager
                    .createNamedQuery("OrderEntity.findByAttendance") //NOI18N
                    .setParameter("attendance", attendance) //NOI18N
                    .setMaxResults(1)
                    .getSingleResult();
            
            return order.getId();
        } catch (NoResultException ex) {
        }

        return null;
    }

    /**
     * Finds the today's default financial activity. It's used to record all
     * operations that are not associated with anybody or anything. If the
     * record does not exist, and createIfDoesNotExist is true, it will be
     * created. Note that the transaction has to be active to create a new 
     * financial activity.
     *
     * @return the ID of the financial activity, or null, if was not found
     * and a new one was not requested.
     * @throws NullPointerException if the createIfDoesNotExist is null
     * @throws IllegalStateException if a new financial activity is required, 
     * but the transaction is not active.
     */
    public Short findCurrentDefault(Boolean createIfDoesNotExist) 
            throws IllegalArgumentException, IllegalStateException, ValidationException {
        
        /*
         * Arguments validation.
         */
        if (createIfDoesNotExist == null) {
            throw new NullPointerException("The createIfDoesNotExist is null."); //NOI18N
        }

        OrderEntity order;

        try {
            order = (OrderEntity)entityManager
                    .createNamedQuery("OrderEntity.findDefaultByDateRecorded") //NOI18N
                    .setParameter("dateRecorded", new Date()) //NOI18N
                    .setMaxResults(1)
                    .getSingleResult();
            return order.getId();
        } catch (NoResultException ex) {
            /*
             * If none was found but a new one was requested, create one.
             */
            if (createIfDoesNotExist) {
                assertTransactionActive();

                order = new OrderEntity();
                order.setId(getNextId());
                order.setDate(new Date());
                order.setPayment(BigDecimal.ZERO);

                // TODO: note change
                entityManager.persist(order);
                entityManager.flush();

                return order.getId();
            }
        }
        return null;
    }
    
    /**
     * Finds financial activities by the date.
     * 
     * <ul>
     * 
     * <li> If the date is not today, the permissions level has to be PL_ALL. </li>
     * 
     * </ul>
     * 
     * @param date  the date
     * @throws IllegalStateException if no session is open
     * @throws SecurityException if current security rules restrict this operation
     * @throws NullPointerException if any of the arguments is null
     */
    public List<OrderDTO> findAllByDate(DateMidnight date) throws SecurityException {
        assertSessionActive();
        
        if(date == null) {
            throw new NullPointerException("The begin is null."); //NOI18N
        }
        
        if(!DateMidnight.now().equals(date) &&
                !sessionService.getPermissionsLevel().equals(SessionsService.PL_ALL)) {
            throw new SecurityException(bundle.getString("AccessDenied"));
        }

        List<OrderEntity> financialActivities = entityManager
                .createNamedQuery("OrderEntity.findByDateRecordedOrderByIdDesc") //NOI18N
                .setParameter("dateRecorded", date.toDate()) //NOI18N
                .getResultList();
        
        List<OrderDTO> result = new LinkedList<>();
        
        for(OrderEntity order : financialActivities) {
            result.add(wrapOrderEntity(order));
        }
        
        return result;
    }
    
    /**
     * Finds financial activities for client within specified time period. 
     * 
     * @param id the client's ID
     * @param begin the beginning date
     * @param end  the ending date
     * @throws IllegalStateException if the transaction is not active, or no session is open
     * @throws NullPointerException if any of the arguments is null
     * @throws ValidationException if the beginning date is after the ending date, or the client's ID is invalid
     */
    public List<OrderDTO> findForClientWithinPeriod(Short id, DateMidnight begin, DateMidnight end) throws ValidationException {
        assertSessionActive();
        
        if(id == null) {
            throw new NullPointerException("The id is null."); //NOI18N
        }
        
        if(begin == null) {
            throw new NullPointerException("The begin is null."); //NOI18N
        }
        
        if(end == null) {
            throw new NullPointerException("The end is null."); //NOI18N
        }
        
        if(begin.isAfter(end)) {
            throw new ValidationException(bundle.getString("BeginningDateAfterEndingDate"));
        }
        
        Client client = entityManager.find(Client.class, id);
        
        if(client == null) {
            throw new ValidationException(bundle.getString("ClientIDInvalid"));
        }
        
        List<OrderEntity> financialActivities = entityManager
                .createNamedQuery("OrderEntity.findByClientAndDateRecordedRangeOrderByDateRecordedDesc") //NOI18N
                .setParameter("client", client) //NOI18N
                .setParameter("rangeBegin", begin.toDate()) //NOI18N
                .setParameter("rangeEnd", end.toDate()) //NOI18N
                .getResultList();
        
        List<OrderDTO> result = new LinkedList<>();
        
        for(OrderEntity order : financialActivities) {
            result.add(wrapOrderEntity(order));
        }
        
        return result;
    }
    
    public BigDecimal getTotalForDate(DateMidnight date) {
        assertSessionActive();
        
        if(date == null) {
            throw new NullPointerException("The date is null."); //NOI18N
        }
        
        BigDecimal result = (BigDecimal)entityManager
                .createNamedQuery("OrderEntity.sumPaymentsForDateRecorded") //NOI18N
                .setParameter("dateRecorded", date.toDate()) //NOI18N
                .getSingleResult();
        
        if(result == null) {
            result = BigDecimal.ZERO;
        }
        
        return result.setScale(2);
    }

    /**
     * Finds the financial activity by its ID.
     *
     * @param id the financial activity's ID.
     * @return the financial activity's information.
     * @throws NullPointerException if the financial activity's ID is null
     * @throws ValidationException if the financial activity's ID is invalid
     * @throws IllegalStateException if the session is not active
     */
    public OrderDTO getById(Short id) throws ValidationException {
        assertSessionActive();
        
        /*
         * Arguments validation.
         */
        if (id == null) {
            throw new NullPointerException("The id is null."); //NOI18N
        }

        /*
         * The idea is to get the financial acivity entity and build
         * OrderDTO from it. The DTO contains some extra fields that
         * the presentation might need.
         */

        OrderEntity order = entityManager.find(OrderEntity.class, id);

        if (order == null) {
            throw new ValidationException(bundle.getString("OrderEntityIDInvalid"));
        }
        
        OrderDTO result = wrapOrderEntity(order);
        
        /*
         * Debt item
         */
        if(order.getClient() != null && isToday(order.getDate())) {
            BigDecimal possibleMoneyBalance = order.getClient().getMoneyBalance().add(result.getTotal());
            if(possibleMoneyBalance.compareTo(BigDecimal.ZERO) < 0) {
                ItemDTO item = new ItemDTO(ItemDTO.FAKE_ID_DEBT, null, bundle.getString("Debt"), null, possibleMoneyBalance.negate().setScale(2));
                result.getItems().add(item);
                result.setTotal(result.getTotal().add(item.getPrice()));
            }
        }
        
        return result;
    }

    /**
     * Adds a purchase to the financial activity. 
     * 
     * <ul>
     * 
     * <li> If the financial activity is not open, the permissions level has
     * to be PL_ALL</li>
     * 
     * <li> If an item subscription is being bought, the financial activity
     * has to be associated with a client.</li>
     * 
     * </ul>
     *
     * @param financialActivitId the financial activity's ID
     * @param itemId the item's ID
     * @throws BusinessException if current security rules restrict this operation
     * @throws NullPointerException if either of the arguments provided is null
     * @throws ValidationException if either of the IDs provided is invalid
     * @throws IllegalStateException if the transaction is not active; if no session is open
     */
    public void addPurchase(Short orderId, Short itemId)
            throws BusinessException, IllegalArgumentException, IllegalStateException, ValidationException, SecurityException {
        assertSessionActive();
        assertTransactionActive();

        /*
         * Arguments validation.
         */
        if(orderId == null) {
            throw new NullPointerException("The finacialActivityId is null."); //NOI18N
        }
        
        if(itemId == null) {
            throw new NullPointerException("The itemId is null."); //NOI18N
        }

        OrderEntity order;
        Item item;
        
        order = entityManager.find(OrderEntity.class,
                    orderId);
        
        if(order == null) {
            throw new ValidationException(bundle.getString("OrderEntityIDInvalid"));
        }
        
        Boolean requiresAllPermissions = (order.getAttendance() != null 
                && !order.getAttendance().getDatetimeEnd().equals(Attendance.DATETIME_END_UNKNOWN))
                || !isToday(order.getDate());

        if(requiresAllPermissions && !sessionService.getPermissionsLevel().equals(SessionsService.PL_ALL)) {
            throw new SecurityException(bundle.getString("OperationDenied"));
        }

        item = entityManager.find(Item.class, itemId);
        
        if(item == null) {
            throw new ValidationException(bundle.getString("ItemIDInvalid"));
        }
        
        if(item.getItemSubscription() != null && order.getClient() == null) {
            throw new BusinessException(bundle.getString("OnlyClientsCanPurchaseSubscriptions"));
        }
        
        /*
         * Checks the item's quantity.
         */
        Short quantity = item.getQuantity();
        if(quantity != null) {
            /*
             * If the item is not in stock, notifies the presentation.
             * Otherwise, decreases the quantity.
             */
            if(quantity == 0) {
                throw new BusinessException(bundle.getString("ItemNotInStock"));
            } else {          
                // TODO: note change
                item.setQuantity(new Integer(quantity-1).shortValue());
            }
        }
        
        /*
         * Business logic specific to financial activities associated with clients.
         */
        if(order.getClient() != null) {
            // TODO: note change
            Client client = order.getClient();
            
            /*
             * Charge the Client's account. Checks whether the new value
             * will overreach the precision limit.
             */
            BigDecimal newMoneyBalance = client.getMoneyBalance().subtract(item.getPrice());
            if (newMoneyBalance.precision() > 5) {
                throw new ValidationException(bundle.getString("LimitReached"));
            }
            client.setMoneyBalance(newMoneyBalance);
            
            if(item.getItemSubscription() != null) {
               /*
                * After the Client has expired, it's attendances balance is kept 
                * until the Client buys another subscription.
                * The attendance's balance is not zeroed, if he buys another
                * subscription before the expiration date.
                */
                short attendancesBalance = client.getAttendancesBalance();
                /*
                 * Expiration base is the date from which we count the expiration
                 * date by adding the Item Subscription's term. It's either today
                 * or the Client's current expiration date, whatever is later.
                 */
                Date expirationBase = client.getExpirationDate();
                
                if(hasExpired(client.getExpirationDate())) {
                    attendancesBalance = 0;
                    expirationBase = new Date();
                }
                
                attendancesBalance += item.getItemSubscription().getUnits();
                client.setAttendancesBalance(attendancesBalance);
                
                client.setExpirationDate(rollExpirationDate(item.getItemSubscription(),
                        expirationBase, true));
                
            }
        }

        /*
         * Attemps to find an order line with the same item.
         */
        OrderLine targetOrderLine = entityManager.find(OrderLine.class, new OrderLineId(orderId, itemId));
        
        /*
         * Creates a new order line, if none with the same item was found.
         */
        if(targetOrderLine == null) {
            targetOrderLine = new OrderLine();
            targetOrderLine.setItem(item);
            targetOrderLine.setOrder(order);
            targetOrderLine.setQuantity((short)1);
            entityManager.persist(targetOrderLine);
            
            List<OrderLine> orderLines = order.getOrderLines();
            if(orderLines == null) {
                orderLines = new LinkedList<>();
            }
            
            orderLines.add(targetOrderLine);
        } else {
            targetOrderLine.setQuantity((short)(targetOrderLine.getQuantity()+1));
        }
        
        entityManager.flush();
    }

    /**
     * Removes one purchase of the item from the financial activity.
     * 
     * <ul>
     * 
     * <li>If the financial activity is not open, the permissions level has to be
     * PL_ALL</li>
     * 
     * <li> The financial activity has to contain at least one purchase of the item.
     * 
     * <li> If the item's ID is negative, it's forced and can not be removed.
     * 
     * <li> The subscriptions can not be removed from the financial activities
     * associated with attendances.
     * 
     * </ul>
     * 
     * @param orderId the financial activity's ID
     * @param itemId the item's ID
     * @throws BusinessException if current business rules restrict this
     * operation
     * @throws SecurityException if current security rules restrict this operation
     * @throws NullPointerException if either of the arguments provided is null
     * @throws ValidationException if either of the IDs provided is invalid
     * @throws IllegalStateException if the transaction is not active, or if no session is open
     */
    public void removePurchase(Short orderId, Short itemId)
            throws BusinessException, IllegalArgumentException, ValidationException, SecurityException {
        assertSessionActive();
        assertTransactionActive();
        
        /*
         * Arguments validation.
         */
        if(orderId == null) {
            throw new NullPointerException("The orderId is null."); //NOI18N
        }
        
        if(itemId == null) {
            throw new NullPointerException("The itemId is null."); //NOI18N
        }
                
        /*
         * If the ID is less than 0, it's fake and can no be removed.
         */
        if(itemId < 0) {
            throw new BusinessException(bundle.getString("ItemEnforcedCanNotBeRemoved"));
        }
        

        OrderEntity order;
        Item item;

        order = entityManager.find(OrderEntity.class,
                    orderId);
        
        if(order == null) {
            throw new ValidationException(bundle.getString("OrderEntityIDInvalid"));
        }
        
        Boolean requiresAllPermissions = (order.getAttendance() != null 
                && !order.getAttendance().getDatetimeEnd().equals(Attendance.DATETIME_END_UNKNOWN))
                || !isToday(order.getDate());

        if(requiresAllPermissions && !sessionService.getPermissionsLevel().equals(SessionsService.PL_ALL)) {
            throw new SecurityException(bundle.getString("OperationDenied"));
        }

        item = entityManager.find(Item.class, itemId);
        
        if(item == null) {
            throw new ValidationException(bundle.getString("ItemIDInvalid"));
        }
        
        
        OrderLine targetOrderLine = entityManager.find(OrderLine.class, new OrderLineId(orderId, itemId));

        if(targetOrderLine == null) {
            throw new BusinessException(bundle.getString("OrderEntityDoesNotContainItem"));
        }
        
        if(order.getAttendance() != null && item.getItemSubscription() != null) {
            throw new BusinessException(bundle.getString("SubscriptionCanNotBeRemovedFromOrderEntityWithAttendance"));
        }
        
        /*
         * Business logic specific to Financial Activities associated with Clients.
         */
        if(order.getClient() != null) {
            // TODO: note change
            Client client = order.getClient();
            
            /*
             * Give money back to the Client.
             */
            client.setMoneyBalance(client.getMoneyBalance().add(item.getPrice()));
            
            if(item.getItemSubscription() != null) {
               /*
                * After the client has expired, it's attendances balance is kept 
                * until the client buys another subscription.
                * The attendance's balance is not zeroed, if he buys another
                * subscription before the expiration date.
                */
                Integer attendancesBalance = client.getAttendancesBalance() - item.getItemSubscription().getUnits();
                client.setAttendancesBalance(attendancesBalance.shortValue());
                
                /*
                 * We count the expiration date by substracting the 
                 * item subscription's term.
                 */
                client.setExpirationDate(rollExpirationDate(item.getItemSubscription(),
                        client.getExpirationDate(), false));
                
            }
        }
        
        /*
         * Restores the item's quantity, if it's finite. It's impossible 
         * to get overflow here, for the item's quantity counter is being 
         * restored to the state it already had before the item was purchased.
         */
        if(item.getQuantity() != null) {
            item.setQuantity((short)(item.getQuantity()+1));
        }
        
        /*
         * Decreases the quantity on the order line, and removes, it if the 
         * quantity is now zero.
         */
        targetOrderLine.setQuantity((short)(targetOrderLine.getQuantity()-1));
        if(targetOrderLine.getQuantity() == 0) {
            // EntityManager won't remove this relationship upon EntityManager.remove call
            order.getOrderLines().remove(targetOrderLine);
            entityManager.remove(targetOrderLine);
        }
        
        entityManager.flush();
    }

    /**
     * Finds all items purchased within the the financial activity.
     *
     * @param orderId the financial activity's ID
     * @return a list of items purchased
     * @throws NullPointerException if the financial activity's ID is null
     * @throws ValidationException if the financial activity's ID is invalid
     * @throws IllegalStateException if the session is not active
     */
    public List<ItemDTO> findPurchases(Short orderId)
            throws ValidationException {
        assertSessionActive();
        
        if(orderId == null) {
            throw new NullPointerException("The orderId is null."); //NOI18N
        }
        
        OrderEntity order;
        LinkedList<ItemDTO> items = new LinkedList<>();

        order = entityManager.find(OrderEntity.class,
                    orderId);
        
        if(order == null) {
            throw new ValidationException(bundle.getString("OrderEntityIDInvalid"));
        }
        
        /*
         * We count the order's total to calculate the client's debt later.
         */
        BigDecimal total = BigDecimal.ZERO;
        
        if(order.getOrderLines() != null) {
            for(OrderLine orderLine : order.getOrderLines()) {
                Item item = orderLine.getItem();
                ItemDTO itemDTO = new ItemDTO(item.getId(), item.getBarcode(), item.getTitle(), item.getQuantity(), item.getPrice());

                for(int i = 0; i < orderLine.getQuantity();i++) {
                    items.add(itemDTO);
                    total = total.add(item.getPrice());
                }
            }
        }
        
        /*
         * Debt item
         */        
        if(order.getClient() != null) {
            BigDecimal possibleMoneyBalance = order.getClient()
                    .getMoneyBalance().add(total.subtract(order.getPayment()));
            if(possibleMoneyBalance.compareTo(BigDecimal.ZERO) < 0) {
                ItemDTO item = new ItemDTO(ItemDTO.FAKE_ID_DEBT, null, bundle.getString("DebtTitle"), null, possibleMoneyBalance.negate().setScale(2));
                items.addFirst(item);
            }
        }
        
        return items;
    }

    /**
     * Records a payment.
     * 
     * <ul>
     * 
     * <li> If the financial activity is closed, the permissions level has to be
     * PL_ALL. </li>
     * 
     * <li>If the financial activity is associated with a client,
     * a withdrawal is allowed, if it won't make the client's money balance 
     * negative. </li>
     * 
     * </ul>
     * 
     * @param orderId the financial activity's ID
     * @param amount the amount paid
     * @throws BusinessException if current business rules restrict this
     * operation
     * @throws NullPointerException if either one of the arguments is null
     * @throws ValidationException if financial activity's ID is invalid
     * @throws SecurityException if current security rules restrict this operation
     * @throws IllegalStateException if the transaction or the session is not active
     */
    public void recordPayment(Short orderId, BigDecimal amount)
            throws BusinessException, IllegalArgumentException, ValidationException, SecurityException {
        assertSessionActive();
        assertTransactionActive();

        /*
         * Checks the arguments.
         */
        if(orderId == null) {
            throw new NullPointerException("The orderId is null."); //NOI18N
        }
        
        if(amount == null) {
            throw new NullPointerException("The amount is null."); //NOI18N
        }
        
        /*
         * Finds the target Financial Activity.
         */
        OrderEntity order = entityManager.find(OrderEntity.class, orderId);
        
        if(order == null) {
            throw new ValidationException(bundle.getString("OrderEntityIDInvalid"));
        }
        
        Boolean requiresAllPermissions = (order.getAttendance() != null 
                && !order.getAttendance().getDatetimeEnd().equals(Attendance.DATETIME_END_UNKNOWN))
                || !isToday(order.getDate());

        if(requiresAllPermissions && !sessionService.getPermissionsLevel().equals(SessionsService.PL_ALL)) {
            throw new SecurityException(bundle.getString("OperationDenied"));
        }
        
        /*
         * Normalizes the scale, and throws an exception, if the scale is 
         * to big.
         */
        if (amount.scale() > 2) {
            throw new ValidationException(bundle.getString("TwoDigitsAfterDecimalPointMax"));
        }
        amount = amount.setScale(2);
        
        
        BigDecimal newTotalPaymentMaid = order.getPayment().add(amount);
        
        if (newTotalPaymentMaid.precision() > 5) {
            throw new ValidationException(bundle.getString("LimitReached"));
        }
        
        /*
         * If the financial activity is associted with a Client,
         * does some checks and alters the Client's money balance.
         */
        if(order.getClient() != null) {
            Client client = order.getClient();
            BigDecimal newMoneyBalance = client.getMoneyBalance().add(amount);
                
            if (newMoneyBalance.precision() > 5) {
                throw new ValidationException(bundle.getString("LimitReached"));
            }
                
            if(newTotalPaymentMaid.compareTo(BigDecimal.ZERO) < 0) {
                if(newMoneyBalance.add(amount).compareTo(BigDecimal.ZERO) < 0) {
                    throw new BusinessException(bundle.getString("ClientNotEnoughMoneyToWithdraw"));
                }
            }
                
            /*
             * Changes the client's money balance.
             * TODO: note change
             */
            client.setMoneyBalance(newMoneyBalance);
        }

        // TODO: note change
        order.setPayment(newTotalPaymentMaid);
        
    }

    /**
     * Returns the amount of payment associated with the financial activity.
     *
     * @param orderId the financial activity's ID
     * @return the amount of payment
     * @throws NullPointerException if the financial activity's
     * ID is null
     * @throws ValidationException if the financial activity's ID is invalid
     * @throws IllegalStateException if the session is not active
     */
    public BigDecimal getPayment(Integer orderId) 
            throws ValidationException {
        assertSessionActive();
        
        /*
         * Arguments validation.
         */
        if(orderId == null) {
            throw new NullPointerException("The orderId is null."); //NOI18N
        }
        
        OrderEntity order = entityManager.find(OrderEntity.class,
                orderId);
        
        if(order == null) {
            throw new ValidationException(bundle.getString("OrderEntityIDInvalid"));
        }
        
        return order.getPayment();
    }

    /**
     * Gets the next free ID that can be assigned to a financial activity.
     * 
     * @return the ID 
     */
    public Short getNextId() {
        try {
            return new Integer((Short) entityManager
                    .createNamedQuery("OrderEntity.findAllIdsOrderByIdDesc") //NOI18N
                    .setMaxResults(1).getSingleResult() + 1)
                    .shortValue();
        } catch (NoResultException ex) {
            return 1;
        }
    }
    
    private OrderDTO wrapOrderEntity(OrderEntity order) {
        
        OrderDTO orderDTO =
                new OrderDTO(
                order.getId(),
                new DateMidnight(order.getDate()),
                order.getPayment().setScale(2));
        
        /*
         * Purchased items
         */
        List<ItemDTO> items = new LinkedList<>();
        
        /*
         * We count the order's total to calculate the client's debt later.
         */
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        
        if(order.getOrderLines() != null) {
            for(OrderLine orderLine : order.getOrderLines()) {
                Item item = orderLine.getItem();
                ItemDTO itemDTO = new ItemDTO(item.getId(), item.getBarcode(), item.getTitle(), item.getQuantity(), item.getPrice());

                for(int i = 0; i < orderLine.getQuantity();i++) {
                    items.add(itemDTO);
                    total = total.add(item.getPrice());
                }
            }
        }
        orderDTO.setItems(items);
        
        /*
         * Client
         */
        if(order.getClient() != null) {
            orderDTO.setClientId(order.getClient().getId());
            orderDTO.setClientFullName(order.getClient().getFullName());
        }
        
        /*
         * Attendance
         */
        if(order.getAttendance() != null) {
            orderDTO.setAttendanceId(order.getAttendance().getId());
            orderDTO.setKeyTitle(order.getAttendance().getKey().getTitle());
        }
        /*
         * Total
         */
        orderDTO.setTotal(total);
        
        /*
         * Due
         */
        orderDTO.setDue(total.subtract(order.getPayment()));
        
        return orderDTO;
    }

    /**
     * Checks whether the provided date is today.
     * 
     * @param date the date to check
     * @return true, if the date's time is past the today's midnight.
     */
    private boolean isToday(Date date) {
        DateMidnight today = new DateMidnight();
        DateMidnight tomorrow = today.plusDays(1);
        return today.getMillis() <= date.getTime() && tomorrow.getMillis() > date.getTime(); 
    }

    /**
     * Returns a <code>Date</code> instance that represents the today's midnight.
     * 
     * @return the <code>Date</code> instance
     */
    private Date getToday() {
        return new DateMidnight().toDate();
    }
    
//    private DateMidnight getTomorrow() {
//        return new DateMidnight().plusDays(1);
//    }
    
    private boolean hasExpired(Date expirationDate) {
        return !new Date().before(expirationDate);
    }
    
    private Date rollExpirationDate(ItemSubscription itemSubscription, Date date, Boolean forward) {
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.setTime(date);
        
        expirationDate.roll(Calendar.YEAR, forward ? itemSubscription.getTermYears() : -itemSubscription.getTermYears());
        expirationDate.roll(Calendar.MONTH, forward ? itemSubscription.getTermMonths() : -itemSubscription.getTermMonths());
        expirationDate.roll(Calendar.DATE, forward ? itemSubscription.getTermDays() : -itemSubscription.getTermDays());
        
        return expirationDate.getTime();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance
     */
    public static OrdersService getInstance() {
        if (instance == null) {
            instance = new OrdersService();
        }
        return instance;
    }
}
