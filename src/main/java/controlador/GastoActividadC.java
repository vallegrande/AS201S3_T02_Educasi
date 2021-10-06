/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.GastoActividadImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.GastoActividadModel;
import servicio.Reporte;

/**
 *
 * @author EDGARD
 */
@Named(value = "gastoActividadC")
@SessionScoped
public class GastoActividadC implements Serializable {

    private GastoActividadModel gasAct;
    private GastoActividadImpl dao;

    private List<GastoActividadModel> listGasAct;
    private List<GastoActividadModel> listAct;

    public GastoActividadC() {
        gasAct = new GastoActividadModel();
        dao = new GastoActividadImpl();
    }

    public void registrar() throws Exception {
        try {
            System.out.println(gasAct);
            dao.registrar(gasAct);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registrado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en modificarC " + e.getMessage());
        }
    }

    public void modificar() throws Exception {
        try {
            dao.modificar(gasAct);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en modificarC " + e.getMessage());
        }
    }

    public void eliminar() throws Exception {
        try {
            dao.eliminar(gasAct);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en eliminarC " + e.getMessage());
        }
    }

    public void reporteGastoRango() throws Exception {
        try {
            if (gasAct.getFechaReportEntrada() == null || gasAct.getFechaReportSalida() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Falta rellenar una fecha en el reporte"));
            }
            if (gasAct.getFechaReportEntrada() != null && gasAct.getFechaReportSalida() != null) {
                if (gasAct.getFechaReportEntrada().after(gasAct.getFechaReportSalida())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Fecha de inicio es mayor a la salida en el reporte"));
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");
                    String sts1 = dateFormat.format(gasAct.getFechaReportEntrada());
                    String sts2 = dateFormat.format(gasAct.getFechaReportSalida());
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date fechaActual = new Date(System.currentTimeMillis());
                    String fechSystem = dateFormat2.format(fechaActual);
                    Reporte report = new Reporte();
                    Map<String, Object> parameters = new HashMap();
                    parameters.put("Parametro1", sts1);
                    parameters.put("Parametro2", sts2);
                    report.exportarPDFGlobal(parameters, "gastoActividadesRango.jasper", fechSystem+" gastoActividadesRango.pdf");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PDF GENERADO", null));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERROR AL GENERAR PDF", null));
            throw e;
        }
    }

    public void reporteGastoActividad() throws Exception {
        try {
            if (gasAct.getFechaReporte() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Falta rellenar la fecha en el reporte"));
            }
            if (gasAct.getFechaReporte() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");
                String sts = dateFormat.format(gasAct.getFechaReporte());
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date fechaActual = new Date(System.currentTimeMillis());
                String fechSystem= dateFormat2.format(fechaActual);
                Reporte report = new Reporte();

                Map<String, Object> parameters = new HashMap();
                parameters.put("Parameter1", sts);
                report.exportarPDFGlobal(parameters, "gastoActividades.jasper",fechSystem+ " gastoActividades.pdf");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PDF GENERADO", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERROR AL GENERAR PDF", null));
            throw e;
        }
    }

    public void limpiar() {
        gasAct = new GastoActividadModel();
    }

    public void listar() {
        try {
            listGasAct = dao.listarTodos();
        } catch (Exception e) {
            System.out.println("Error en listarC " + e.getMessage());
        }
    }

    public void obtenerCuota() throws Exception {

        try {
            if (gasAct.getFKactividad() > 0) {
                gasAct.setCantGasActividad(dao.obtenerSaldoActividad(gasAct.getFKactividad()));
            }
        } catch (Exception e) {
            System.out.println("Error en obtener cuota " + e.getMessage());
        }

    }

    public GastoActividadModel getGasAct() {
        return gasAct;
    }

    public void setGasAct(GastoActividadModel gasAct) {
        this.gasAct = gasAct;
    }

    public GastoActividadImpl getDao() {
        return dao;
    }

    public void setDao(GastoActividadImpl dao) {
        this.dao = dao;
    }

    public List<GastoActividadModel> getListGasAct() {
        return listGasAct;
    }

    public void setListGasAct(List<GastoActividadModel> listGasAct) {
        this.listGasAct = listGasAct;
    }

    public List<GastoActividadModel> getListAct() {
        try {
            listAct = dao.listarAct();
        } catch (SQLException ex) {
            Logger.getLogger(CuotaC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CuotaC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listAct;
    }

    public void setListAct(List<GastoActividadModel> listAct) {
        this.listAct = listAct;
    }

}
