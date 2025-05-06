package repository;

import domain.Employee;
import domain.EmployeeType;
import domain.validator.RepoException;
import domain.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class EmployeeRepositoryDB implements IEmployeeRepository {
    private JDBCUtils jdbcUtils;
    private Validator<Employee> validator;
    private static final Logger logger = LogManager.getLogger(EmployeeRepositoryDB.class);

    public EmployeeRepositoryDB(Properties properties, Validator<Employee> valiator) {
        logger.info("Initializing Employee Repository");
        this.jdbcUtils = new JDBCUtils(properties);
        this.validator = valiator;
    }

    @Override
    public Optional<Employee> findOne(Integer integer) {
        logger.info("Finding employee by id: {}", integer);
        Connection connection = jdbcUtils.getConnection();
        Employee employee = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Employees WHERE id = ?")) {
            preparedStatement.setInt(1, integer);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                employee = new Employee(name, username, password);
                employee.setId(id);
            }
        }catch (SQLException e) {
            logger.error(e);
            System.out.println("Find employee by id: " + integer + " ERROR");
        }
        return Optional.ofNullable(employee);
    }

    @Override
    public List<Employee> findAll() {
        logger.info("Finding all employees");
        Connection connection = jdbcUtils.getConnection();
        List<Employee> employees = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Employees WHERE type = 'EMPLOYEE'")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Employee employee = new Employee(name, username, password);
                employee.setId(id);
                employees.add(employee);
            }
        }catch (SQLException e) {
            logger.error(e);
            System.out.println("Finding all employees - ERROR");
        }
        return employees;
    }

    @Override
    public Optional<Employee> findByUsername(String username) {
        logger.info("Finding employee by username: {}", username);
        Connection connection = jdbcUtils.getConnection();
        Employee employee = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Employees WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                EmployeeType type = EmployeeType.valueOf(resultSet.getString("type"));
                employee = new Employee(name, username, password);
                employee.setId(id);
                employee.setType(type);
            }
        }catch (SQLException e) {
            logger.error(e);
            System.out.println("Find employee by username: " + username + " ERROR");
        }
        return Optional.ofNullable(employee);
    }

    @Override
    public Optional<Employee> save(Employee entity) throws RepoException {
        logger.info("Saving employee: " + entity);
        String errs = validator.validate(entity).trim();
        if(!errs.isEmpty()) {
            logger.error(errs);
            throw new RepoException(errs);
        }
        Connection connection = jdbcUtils.getConnection();
        int result = 0;
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Employees(name,username,password,type) values(?,?,?,?)")){
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4,entity.getType().toString());
            result = preparedStatement.executeUpdate();
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Saving employee: " + entity+ "ERROR");
        }
        if(result < 0){
            return Optional.of(entity);
        }
        logger.info("Employee: "+entity+ " - saved successfully");
        return Optional.empty();
    }
}
