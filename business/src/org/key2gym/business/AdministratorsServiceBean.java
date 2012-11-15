package org.key2gym.business;

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



import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import org.key2gym.business.api.ValidationException;
import org.key2gym.business.api.dtos.AdministratorDTO;
import org.key2gym.business.api.remote.AdministratorsServiceRemote;
import org.key2gym.persistence.Administrator;

/**
 *
 * @author Danylo Vashchilenko
 */
@Stateless
@Remote(AdministratorsServiceRemote.class)
@DeclareRoles({SecurityRoles.MANAGER, SecurityRoles.JUNIOR_ADMINISTRATOR, SecurityRoles.SENIOR_ADMINISTRATOR})
@RolesAllowed({SecurityRoles.MANAGER, SecurityRoles.JUNIOR_ADMINISTRATOR, SecurityRoles.SENIOR_ADMINISTRATOR})
public class AdministratorsServiceBean extends BasicBean implements AdministratorsServiceRemote {

    @Override
    public AdministratorDTO getById(Integer id) {
        
        if (id == null) {
            throw new NullPointerException("The id is null."); //NOI18N
        }
        
        Administrator entityAdministrator = getEntityManager().find(Administrator.class, id);
        
        AdministratorDTO administrator = new AdministratorDTO();
        administrator.setFullName(entityAdministrator.getFullName());
        administrator.setId(entityAdministrator.getId());
        administrator.setNote(entityAdministrator.getNote());
        administrator.setUserName(entityAdministrator.getUsername());
        
        return administrator;
    }

    @Override
    public AdministratorDTO getByUsername(String username) throws ValidationException {
                
        if (username == null) {
            throw new NullPointerException("The username is null."); //NOI18N
        }
        
        Administrator entityAdministrator;
        
        try {
            entityAdministrator = (Administrator)getEntityManager()
                .createNamedQuery("Administrator.findByUsername")
                .setParameter("username", username)
                .getSingleResult();
        } catch(NoResultException ex) {
            throw new ValidationException(getString("Invalid.Administrator.Username"));
        }
        
        AdministratorDTO administrator = new AdministratorDTO();
        administrator.setFullName(entityAdministrator.getFullName());
        administrator.setId(entityAdministrator.getId());
        administrator.setNote(entityAdministrator.getNote());
        administrator.setUserName(entityAdministrator.getUsername());
        
        return administrator;
    }
}