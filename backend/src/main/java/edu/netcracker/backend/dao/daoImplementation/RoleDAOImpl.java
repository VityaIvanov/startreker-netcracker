package edu.netcracker.backend.dao.daoImplementation;

import edu.netcracker.backend.dao.daoInterface.CrudDAO;
import edu.netcracker.backend.dao.daoInterface.RoleDAO;
import edu.netcracker.backend.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl extends CrudDAO<Role> implements RoleDAO {

}

