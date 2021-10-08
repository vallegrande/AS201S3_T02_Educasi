/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.ICRUD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import modelo.PersonaModel;

/**
 *
 * @author ZERO
 */
public class PersonaImpl extends Conexion implements ICRUD<PersonaModel>{

    @Override
    public void registrar(PersonaModel per) throws Exception {
       String sql = "insert into PERSONA (NOMPER, APEPER, PASPER, EMAPER, DNIPER, CELPER, ROLPER,IDUBG,PERSONA_IDPER)values (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, per.getNombre());
            ps.setString(2, per.getApellido());
            ps.setString(3, per.getPassword());
            ps.setString(4, per.getEmail());
            ps.setString(5, per.getDNI());
            ps.setString(6, per.getCelular());
            ps.setString(7, per.getROL());
            ps.setString(8, per.getUbigeoFK());
            if (per.getPersonaID()==0) {
                ps.setNull(9, Types.INTEGER);
            }else{
                ps.setInt(9, per.getPersonaID());
            }
            
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al Ingresar Persona Dao " + e.getMessage());
        }finally{
            this.Cerrar();
        }
    }
    public void registrarD(PersonaModel per) throws SQLException {
       String sql = "insert into PERSONA (NOMPER, APEPER, PASPER, EMAPER, DNIPER, CELPER, ROLPER,IDUBG,PERSONA_IDPER)values (?,?,?,?,?,?,?,?,?)";
        try (Connection conec  = (Connection) this.getCn() ) {
            PreparedStatement ps = conec.prepareStatement(sql);
            ps.setString(1, per.getNombre());
            ps.setString(2, per.getApellido());
            ps.setString(3, per.getPassword());
            ps.setString(4, per.getEmail());
            ps.setString(5, per.getDNI());
            ps.setString(6, per.getCelular());
            ps.setString(7, per.getROL());
            ps.setString(8, per.getUbigeoFK());
            if (per.getPersonaID()==0) {
                ps.setNull(9, Types.INTEGER);
            }else{
                ps.setInt(9, per.getPersonaID());
            }
            ps.executeUpdate();
            ps.close();
        } 
    }
    @Override
    public void modificar(PersonaModel per) throws Exception {
       String sql = "update PERSONA set NOMPER=?, APEPER=?, PASPER=?, EMAPER=?, DNIPER=?, CELPER=?, ROLPER=?,ESTPER=?,IDUBG=? where IDPER=?";
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, per.getNombre());
            ps.setString(2, per.getApellido());
            ps.setString(3, per.getPassword());
            ps.setString(4, per.getEmail());
            ps.setString(5, per.getDNI());
            ps.setString(6, per.getCelular());
            ps.setString(7, per.getROL());
            ps.setString(8, per.getEstado());
            ps.setString(9, per.getUbigeoFK());
            ps.setInt(10, per.getID());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al modificar Persona Dao " + e.getMessage());
        }finally {
            this.Cerrar();
        }
    }

    @Override
    public void eliminar(PersonaModel per) throws Exception {
     String sql = "delete from PERSONA where IDPER=?";               
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);             
            ps.setInt(1, per.getID());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en eliminarD" + e.getMessage());
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<PersonaModel> listarTodos() throws Exception {
       List<PersonaModel> listado = null;
        PersonaModel per;
        String sql = "SELECT* FROM V_PERSONA";
        ResultSet rs;
        try (Connection conec  = (Connection) this.getCn() ){
            this.conectar();
            listado = new ArrayList();
            PreparedStatement ps = conec.prepareStatement(sql);
            rs = ps.executeQuery(); 
            while (rs.next()) {
                per = new PersonaModel();
                per.setFila(rs.getString("FILA"));
                per.setID(rs.getInt("IDPER"));
                per.setNombre(rs.getString("NOMPER"));
                per.setApellido(rs.getString("APEPER"));
                per.setPassword(rs.getString("PASPER"));
                per.setEmail(rs.getString("EMAPER"));
                per.setDNI(rs.getString("DNIPER"));
                per.setCelular(rs.getString("CELPER"));
                per.setROL(rs.getString("ROLPER"));
                per.setEstado(rs.getString("ESTPER"));
                per.setUbigeoFK(rs.getString("UBIGEO"));
                per.setRelacion(rs.getString("RELACION"));
                //per.setPersonaID(rs.getInt("PERSONA_IDPER"));
                listado.add(per);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en listarTodos Dao" + e.getMessage());
        }
        return listado;
    }
    public List<PersonaModel> listarUbigeo()throws SQLException{
    List<PersonaModel> listadoUbigeo = null;
    PersonaModel per;
    ResultSet rs;
    String sql ="select*from ubigeo";
        try {
            listadoUbigeo = new ArrayList();
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()) {
                per= new PersonaModel();
                per.setIDubigeo(rs.getInt("IDUBG"));
                per.setCodigoUbigeo(rs.getString("CODUBG"));
                per.setDeparUbigeo(rs.getString("DEPUBG"));
                per.setProvUbigeo(rs.getString("PROUBG"));
                per.setDistUbigeo(rs.getString("DISUBG"));
                listadoUbigeo.add(per);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en listarUbigeo Dao" + e.getMessage());
        }
        return listadoUbigeo;
    }
    public List<PersonaModel> ListarApoderados() throws SQLException{
        List<PersonaModel> listadoA = null;
        PersonaModel per;
        ResultSet rs;
        String sql = "select IDPER,NOMPER,APEPER from PERSONA WHERE ROLPER='APODERADO'";
        try {
            listadoA = new ArrayList();
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            rs = ps.executeQuery(); 
            while (rs.next()) {
                per = new PersonaModel();
                per.setID(rs.getInt("IDPER"));
                per.setNombre(rs.getString("NOMPER"));
                per.setApellido(rs.getString("APEPER"));
                listadoA.add(per);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en listarApoderado Dao" + e.getMessage());
        }
        return listadoA;
    }
    public List<PersonaModel> ListarPorRol(String rol) throws SQLException{
       
        List<PersonaModel> listadoRol = null;
        PersonaModel per;
        String sql = "";
        switch(rol){
            case "ADMIN":
                sql = "select * from V_PERSONA_ROL WHERE ROLPER='ADMIN'";
                break;
            case "APODERADO":
                sql = "select * from V_PERSONA_ROL WHERE ROLPER='APODERADO'";
                break;
            case "ALUMNO":
                sql = "select * from V_PERSONA_ROL WHERE ROLPER='ALUMNO'";
                break;
        }
         try {
            listadoRol = new ArrayList();
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                per = new PersonaModel();
                per.setFila(rs.getString("FILA"));
                per.setID(rs.getInt("IDPER"));
                per.setNombre(rs.getString("NOMPER"));
                per.setApellido(rs.getString("APEPER"));
                per.setPassword(rs.getString("PASPER"));
                per.setEmail(rs.getString("EMAPER"));
                per.setDNI(rs.getString("DNIPER"));
                per.setCelular(rs.getString("CELPER"));
                per.setROL(rs.getString("ROLPER"));
                per.setEstado(rs.getString("ESTPER"));
                per.setUbigeoFK(rs.getString("UBIGEO"));
                per.setRelacion(rs.getString("RELACION"));
                listadoRol.add(per);
            }
            rs.close();
            ps.close();
        }catch (Exception e) {
            System.out.println("Error en listar por Rol Dao" + e.getMessage());
        }
        return listadoRol;
    }
    
    public static void main(String[] args) {
        PersonaImpl lists = new PersonaImpl();
        try {
            for (PersonaModel ListarApoderado : lists.ListarApoderados()) {
                System.out.println("ss= "+ ListarApoderado);
            }
            lists.ListarApoderados();
        } catch (Exception e) {
        }
    }

    
}
