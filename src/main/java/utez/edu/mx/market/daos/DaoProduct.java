package utez.edu.mx.market.daos;

import utez.edu.mx.market.entities.Product;
import utez.edu.mx.market.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoProduct {
    private Connection con;
    private PreparedStatement pstm;
    private ResultSet rs;
    private final DBConnection DB_CONNECTION = new DBConnection();
    private final DaoCategory DAO_CATEGORY = new DaoCategory();
    private final String[] QUERIES = {
            "SELECT * FROM product;",
            "SELECT * FROM PRODUCT WHERE ID = ?;",
            "INSERT INTO PRODUCT (name, description, stock, on_sale,id_category) VALUES (?,?,?,?,?);",
            "UPDATE PRODUCT SET name = ?, description= ?, stock= ?, on_sale = ?, id_category= ? WHERE id =?;",
            "UPDATE PRODUCT SET on_sale = ? WHERE id = ?;",
            "DELETE FROM PRODUCT WHERE id = ? ;"
    };

    public List <Product> findAllProduct(){
        List<Product> list = new ArrayList<>();
        try {
            con = DB_CONNECTION.getConnection();
            pstm= con.prepareStatement(QUERIES[0]);
            rs = pstm.executeQuery();

            while (rs.next()){
                Product product = new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getLong("stock"),
                        rs.getBoolean("on_sale"),
                        DAO_CATEGORY.findCategoryBYyId(rs.getInt("id_category"))
                );
                list.add(product);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            CloseConnection();
        }

        return list;
    }

    public Product findProductById (long id){
        Product found = null;
        try {
            con = DB_CONNECTION.getConnection();
            pstm= con.prepareStatement(QUERIES[1]);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if (rs.next()){
                found = new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getLong("stock"),
                        rs.getBoolean("on_sale"),
                        DAO_CATEGORY.findCategoryBYyId(rs.getInt("id_category"))
                );
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            CloseConnection();
        }

        return found;
    }

    public  boolean createProduct (Product product){
        try {
            con = DB_CONNECTION.getConnection();
            pstm= con.prepareStatement(QUERIES[2]);
            pstm.setString(1,product.getName());
            pstm.setString(2,product.getDescription());
            pstm.setLong(3,product.getStock());
            pstm.setBoolean(4,product.isOnSale());
            pstm.setInt(5,product.getCategory().getId());
            return pstm.executeUpdate() == 1;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            CloseConnection();
        }
    }

    public  boolean updateProduct (Product product){
        Product found = findProductById(product.getId());
        if (found != null) {
            try {
                con = DB_CONNECTION.getConnection();
                pstm = con.prepareStatement(QUERIES[3]);
                pstm.setString(1, product.getName());
                pstm.setString(2, product.getDescription());
                pstm.setLong(3, product.getStock());
                pstm.setBoolean(4, product.isOnSale());
                pstm.setInt(5, product.getCategory().getId());
                pstm.setLong(6, product.getId());
                return pstm.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                CloseConnection();
            }
        }else {
            return  false;
        }
    }

    public  boolean changeStatusProduct (long id){
        Product found = findProductById(id);
        if (found != null) {
            try {
                con = DB_CONNECTION.getConnection();
                pstm = con.prepareStatement(QUERIES[4]);
                pstm.setBoolean(1, !found.isOnSale());
                pstm.setLong(2, id);
                return pstm.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                CloseConnection();
            }
        }else {
            return  false;
        }
    }

    public  boolean deleteProduct (long id){
        if (findProductById(id) != null){
            try {
                con = DB_CONNECTION.getConnection();
                pstm = con.prepareStatement(QUERIES[5]);
                pstm.setLong(1, id);
                return pstm.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                CloseConnection();
            }
        }else {
            return  false;
        }
    }

    public void CloseConnection(){
        try {
            if (con!=null){
                con.close();
            }
            if (pstm!=null){
                pstm.close();
            }
            if (rs!=null){
                rs.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}