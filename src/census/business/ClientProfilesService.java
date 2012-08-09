/*
 * Copyright 2012 Danylo Vashchilenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package census.business;

import census.business.api.BusinessException;
import census.business.api.SecurityException;
import census.business.api.ValidationException;
import census.business.api.Validator;
import census.business.dto.ClientProfileDTO;
import census.persistence.AdSource;
import census.persistence.Client;
import census.persistence.ClientProfile;
import org.joda.time.DateMidnight;
import org.joda.time.Instant;

/**
 *
 * @author Danylo Vashchilenko
 */
public class ClientProfilesService extends BusinessService {

    /**
     * Updates a client profile. 
     * <p>
     * 
     * <ul>
     * <li> The client profile's ID is also the client's ID </li>
     * <li> If the client does not have a profile, it will be created </li>
     * </ul>
     *
     * @param clientProfile the Client Profile
     * @throws NullPointerException if any of arguments or required properties
     * is null
     * @throws IllegalStateException if the transaction or the session is not active
     * @throws BusinessException if current business rules restrict this
     * operation
     * @throws ValidationException if any of the required properties is invalid
     */
    public void updateClientProfile(ClientProfileDTO clientProfile) throws BusinessException, ValidationException {
        assertTransactionActive();
        assertOpenSessionExists();
        
        if (clientProfile == null) {
            throw new NullPointerException("The clientProfile is null."); //NOI18N
        } else if(clientProfile.getClientId() == null) {
            throw new NullPointerException("The clientProfile.getClientId() is null."); //NOI18N
        } else if (clientProfile.getAddress() == null) {
            throw new NullPointerException("The clientProfile.getAddress() is null."); //NOI18N
        } else if (clientProfile.getTelephone() == null) {
            throw new NullPointerException("The clientProfile.getTelephone() is null."); //NOI18N
        } else if (clientProfile.getFavouriteSport() == null) {
            throw new NullPointerException("The clientProfile.getFavouriteSport() is null."); //NOI18N
        } else if (clientProfile.getGoal() == null) {
            throw new NullPointerException("The clientProfile.getGoal() is null."); //NOI18N
        } else if (clientProfile.getHealthRestrictions() == null) {
            throw new NullPointerException("The clientProfile.getHealthRestrictions() is null."); //NOI18N
        } else if (clientProfile.getPossibleAttendanceRate() == null) {
            throw new NullPointerException("The clientProfile.getPossibleAttendanceRate() is null."); //NOI18N
        } else if (clientProfile.getSpecialWishes() == null) {
            throw new NullPointerException("The clientProfile.getSpecialWishes() is null."); //NOI18N
        }
        
        getHeightValidator().validate(clientProfile.getHeight());
        getWeightValidator().validate(clientProfile.getWeight());
        
        /*
         * Birthday
         */
        getBirthdayValidator().validate(clientProfile.getBirthday());
        if(clientProfile.getBirthday() == null) {
            clientProfile.setBirthday(new DateMidnight(ClientProfile.defaultBirthday));
        }
        
        getAdSourceIdValidator().validate(clientProfile.getAdSourceId());
        
        /*
         * Builds an exact copy of the entity, because it's not a good 
         * practive to make entites instances used as DTO managed.
         */
        ClientProfile entityClientProfile = 
                new ClientProfile(
                clientProfile.getClientId(), 
                ClientProfile.Sex.values()[clientProfile.getSex().ordinal()], 
                clientProfile.getBirthday().toDate(), 
                clientProfile.getAddress(), 
                clientProfile.getTelephone(), 
                clientProfile.getGoal(),
                clientProfile.getPossibleAttendanceRate(), 
                clientProfile.getHealthRestrictions(), 
                clientProfile.getFavouriteSport(), 
                ClientProfile.FitnessExperience.values()[clientProfile.getFitnessExperience().ordinal()], 
                clientProfile.getSpecialWishes(), 
                clientProfile.getHeight(),
                clientProfile.getWeight(), 
                clientProfile.getAdSourceId());
        
        if(entityManager.find(ClientProfile.class, clientProfile.getClientId()) == null) {
            entityManager.persist(entityClientProfile);
        } else {
            entityManager.merge(entityClientProfile);
        }
        
        entityManager.flush();
    }
    
    /**
     * Detaches the profile from the client by its ID.
     * <p>
     * 
     * <ul>
     * <li> The permissions level has to be PL_ALL </li>
     * <li> The client has to have a profile attached </li>
     * </ul>
     * 
     * @param id the client's ID whose profile to detach
     * @throws IllegalStateException if the transaction is not active; if no session is open
     * @throws SecurityException if current security rules restrict this operation
     * @throws NullPointerException if the id is null
     * @throws ValidationException if the id is invalid
     * @throws BusinessException if current business rules restrict this operation
     */
    public void detachClientProfile(Short id) throws SecurityException, ValidationException, BusinessException {
        assertOpenSessionExists();
        assertTransactionActive();
        
        if(!sessionService.getPermissionsLevel().equals(SessionsService.PL_ALL)) {
            throw new SecurityException(bundle.getString("OperationDenied"));
        }
        
        if(id == null) {
            throw new NullPointerException("The id is null."); //NOI18N
        }
        
        Client client = entityManager.find(Client.class, id);
        
        if(client == null) {
            throw new ValidationException(bundle.getString("IDInvalid"));
        }
        
        if(client.getClientProfile() == null) {
            throw new BusinessException(bundle.getString("ClientHasNoProfile"));
        }
        
        entityManager.remove(client.getClientProfile());
        entityManager.flush();
    }
    
    /**
     * Gets a client profile by ID.
     * 
     * @param id the client profile's ID
     * @return the client profile
     * @throws ValidationException the ID is invalid 
     * @throws IllegalStateException if no session is open
     */
    public ClientProfileDTO getById(Short id) throws ValidationException {
        assertOpenSessionExists();
        
        if(id == null) {
            throw new NullPointerException("The id is null."); //NOI18N
        }
        
        ClientProfile clientProfile = entityManager.find(ClientProfile.class, id);
        
        if(clientProfile == null) {
            throw new ValidationException(bundle.getString("ClientProfileIDInvalid"));
        }
        
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO(
                clientProfile.getId(), 
                ClientProfileDTO.Sex.values()[clientProfile.getSex().ordinal()], 
                clientProfile.getBirthday().equals(ClientProfile.defaultBirthday) ? null : new DateMidnight(clientProfile.getBirthday()), 
                clientProfile.getAddress(), 
                clientProfile.getTelephone(), 
                clientProfile.getGoal(),
                clientProfile.getPossibleAttendanceRate(), 
                clientProfile.getHealthRestrictions(), 
                clientProfile.getFavouriteSport(), 
                ClientProfileDTO.FitnessExperience.values()[clientProfile.getFitnessExperience().ordinal()], 
                clientProfile.getSpecialWishes(), 
                clientProfile.getHeight(),
                clientProfile.getWeight(), 
                clientProfile.getAdSourceId());
        
        return clientProfileDTO;
    }
    
    private Validator getBirthdayValidator() {
        return new Validator<DateMidnight>() {

            @Override
            public void validate(DateMidnight value) throws ValidationException {
                if (value != null && value.isAfter(new Instant())) {
                    throw new ValidationException(bundle.getString("ClientProfileBirthdayMustBeInPast"));
                }
            }
        };
    }

    private Validator getAdSourceIdValidator() {
        return new Validator<Short>() {

            @Override
            public void validate(Short value) throws ValidationException {
                if (value == null) {
                    throw new NullPointerException("The ad source's ID is null."); //NOI18N
                }

                AdSource adSource = entityManager.find(AdSource.class, value);

                if (adSource == null) {
                    throw new ValidationException(bundle.getString("AdSourceIDInvalid"));
                }
            }
        };
    }

    private Validator getHeightValidator() {
        return new Validator<Short>() {

            @Override
            public void validate(Short value) throws ValidationException {
                if (value == null) {
                    throw new NullPointerException("The height is null."); //NOI18N
                }

                if (value < 0) {
                    throw new ValidationException(bundle.getString("ClientProfileHeightCanNotBeNegative"));
                }
            }
        };
    }

    private Validator getWeightValidator() {
        return new Validator<Short>() {

            @Override
            public void validate(Short value) throws ValidationException {
                if (value == null) {
                    throw new NullPointerException("The weight is null."); //NOI18N
                }

                if (value < 0) {
                    throw new ValidationException(bundle.getString("ClientProfileWeightCanNotBeNegative"));
                }
            }
        };
    }
    
    /**
     * Singleton instance.
     */
    private static ClientProfilesService instance;

    /**
     * Gets an instance of this class.
     * 
     * @return an instance of this class 
     */
    public static ClientProfilesService getInstance() {
        if (instance == null) {
            instance = new ClientProfilesService();
        }
        return instance;
    }
}
