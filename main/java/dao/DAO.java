package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * interface that specifies what methods will need to be standard for each implementation
 *
 * @param <T> class that will need to implement these methods
 */
public interface DAO<T> {
    /**
     * intended to get a certain instance of a class identified by an id
     *
     * @param id the id to search for the object
     * @return the instance of the class if the id has a match
     * @throws SQLException
     */
    T get(int id) throws SQLException;

    /**
     * intended to return all instances of a certain class
     *
     * @return a list of all instances of a certain class
     * @throws SQLException
     */
    List<T> getALL() throws SQLException;

    /**
     * intended to insert a new instance of the class
     *
     * @param t instance of what class is being passed through (like an appointment)
     * @return a certain int depending on if an item was inserted or not
     * @throws SQLException
     */
    int insert(T t) throws SQLException;

    /**
     * intended to update an instance of the class
     *
     * @param t instance of what class is being passed through (like an appointment)
     * @return a certain int depending on if an item was updated or not
     * @throws SQLException
     */
    int update(T t) throws SQLException;

    /**
     * intended to delete an instance of the class
     *
     * @param t instance of what class is being passed through (like an appointment)
     * @return a certain int depending on if an item was deleted or not
     * @throws SQLException
     */
    int delete(T t) throws SQLException;
}
