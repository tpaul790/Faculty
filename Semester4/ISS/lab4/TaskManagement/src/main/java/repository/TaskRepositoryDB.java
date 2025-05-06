package repository;

import domain.Employee;
import domain.Task;
import domain.validator.RepoException;
import domain.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class TaskRepositoryDB implements ITaskRepository {
    private JDBCUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(TaskRepositoryDB.class);
    private Validator<Task> validator;

    public TaskRepositoryDB(Properties properties, Validator<Task> validator) {
        logger.info("Initializing Task Repository");
        this.jdbcUtils = new JDBCUtils(properties);
        this.validator = validator;
    }

    @Override
    public Optional<Task> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        logger.info("Finding all tasks");
        Connection connection = jdbcUtils.getConnection();
        List<Task> tasks = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tasks where solveTime ISNULL")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                String createTime = resultSet.getString("createTime");
                String solveTime = resultSet.getString("solveTime");
                Task task = new Task(description);
                task.setId(id);
                task.setCreateTime(LocalDateTime.parse(createTime));
                task.setSolveTime(solveTime == null ? null : LocalDateTime.parse(solveTime));
                tasks.add(task);
            }
        }catch (SQLException e) {
            logger.error(e);
            System.out.println("Finding all tasks - ERROR");
        }
        return tasks;
    }

    @Override
    public Optional<Task> save(Task entity) throws RepoException {
        logger.info("Saving task: " + entity);
        String errs = validator.validate(entity).trim();
        if (!errs.isEmpty()) {
            logger.error(errs);
            throw new RepoException(errs);
        }
        Connection connection = jdbcUtils.getConnection();
        int result = 0;
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Tasks(description,createTime) values(?,?)")){
            preparedStatement.setString(1, entity.getDescription());
            preparedStatement.setString(2, entity.getCreateTime().toString());
            result = preparedStatement.executeUpdate();
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Saving task: " + entity+ "ERROR");
        }
        if(result < 0){
            return Optional.of(entity);
        }
        logger.info("Task: "+entity+ " - saved successfully");
        return Optional.empty();
    }

    @Override
    public Optional<Task> update(Task task) throws RepoException {
        logger.info("Updating task: " + task);
        String errs = validator.validate(task).trim();
        if (!errs.isEmpty()) {
            logger.error(errs);
            throw new RepoException(errs);
        }
        Connection connection = jdbcUtils.getConnection();
        int result = 0;
        try(PreparedStatement preparedStatement = connection.prepareStatement("update Tasks set assignedEmployeeId = ?, solveTime = ? where id = ?")){
            preparedStatement.setInt(1,task.getAssignetEmployeeId());
            preparedStatement.setString(2,task.getSolveTime() == null ? null : task.getSolveTime().toString());
            preparedStatement.setInt(3,task.getId());
            result = preparedStatement.executeUpdate();
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Updating task: " + task+ "ERROR");
        }
        if(result < 0){
            return Optional.of(task);
        }
        logger.info("Task: "+task+ " - updated successfully");
        return Optional.empty();
    }
}
