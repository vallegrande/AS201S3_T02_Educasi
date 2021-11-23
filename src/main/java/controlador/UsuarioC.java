/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.UsuarioImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import modelo.UsuarioModel;
import static servicio.MailJava.notificarCorreo;

/**
 *
 * @author EDGARD
 */
@Data
@Named(value = "usuarioC")
@SessionScoped
public class UsuarioC implements Serializable {

    UsuarioImpl dao;
    UsuarioModel usuarrio;
    String user;
    String pass;
    int captcha = 0;
    int intentos = 0;
    boolean bloquear = false;

    public UsuarioC() {
        usuarrio = new UsuarioModel();
        dao = new UsuarioImpl();
    }

    public void ingres() throws Exception {
        try {
            usuarrio = dao.ingresoLogin(usuarrio.getDNI(), usuarrio.getPass());
            System.out.println(usuarrio.getDNI());
            System.out.println(usuarrio.getEmail());
            System.out.println(usuarrio.getRol());
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "Error en login_C {0} ", e.getMessage());
            e.printStackTrace();
        }
    }

    public void acceso() throws Exception {
        try {

            if (dao.logueo == false) {
                this.ingres();
                intentos++;
                switch (intentos) {
                    case 1:
                        setIntentos(1);
                        setCaptcha(0);
                        Logger.getGlobal().log(Level.INFO, "intentos igual {} ", intentos);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "1 INTENTO FALLIDO", "Usuario/Contraseña incorrectas"));
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "LE QUEDAN 2 INTENTOS", ""));
                        break;
                    case 2:
                        setIntentos(2);
                        setCaptcha(1);
                        Logger.getGlobal().log(Level.INFO, "intentos igual {} ", intentos);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "2 INTENTO FALLIDO", "Usuario/Contraseña incorrectas"));
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "LE QUEDA 1 INTENTO", ""));
                        break;
                    case 3:
                        Logger.getGlobal().log(Level.INFO, "intentos igual {} ", intentos);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "3 INTENTO FALLIDO", "Usuario/Contraseña incorrectas"));
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "BLOQUEO DE SEGURIDAD", ""));
                        setIntentos(3);

                        bloquear = true;
                        if (bloquear) {
                            delaySegundo();
                        }
                        if (intentos == 3) {
                            setIntentos(0);
                            setCaptcha(0);

                        }
                        break;
                    default:
                        break;
                }
            } else {
                
                this.ingres();
                if (usuarrio.getRol() != null) {
                    System.out.println("no entra");
                    if ("ADMIN    ".equals(usuarrio.getRol())) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡BIENVENIDO!", "Ingreso Exitoso"));
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/AS201S3_T02_Educasi/faces/vistas/menuContenido.xhtml");
                        notificarCorreo(usuarrio);
                    }
                    if ("APODERADO".equals(usuarrio.getRol())) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡BIENVENIDO!", "Ingreso Exitoso"));
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/AS201S3_T02_Educasi/faces/vistas/menuContenido2.xhtml");
                        notificarCorreo(usuarrio);
                    }
                    if ("ALUMNO   ".equals(usuarrio.getRol())) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡BIENVENIDO!", "Ingreso Exitoso"));
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/AS201S3_T02_Educasi/faces/vistas/menuContenido2.xhtml");
                        notificarCorreo(usuarrio);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "Error en Acceso_C {0} ", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void delaySegundo() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "Error en delaySegundo_C {0} ", e.getMessage());
            e.printStackTrace();
        }
    }

}
