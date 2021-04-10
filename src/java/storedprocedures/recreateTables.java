/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storedprocedures;

import database.dbConnweb;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.String;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author EKaunda
 */
public class recreateTables extends HttpServlet {

    HttpSession session=null;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
            
            session= request.getSession();
            
            
           String [] storedprocedures={"call openmrs.create_etl_tables();","call openmrs.sp_first_time_setup();","call openmrs.create_datatools_tables();"};
           
           
           
           dbConnweb conn = new dbConnweb();
           
           int a=0;
           
            for (String  sp : storedprocedures) 
            {
                a++;
              conn.rs=conn.st.executeQuery(sp);
              
              while(conn.rs.next()){
              
                  String status=conn.rs.getString(1);
                  
                  System.out.println(status);
                  
                  session.setAttribute("querystatus",status);
                  
              }
                                
            }
           
            out.println("Recreating Tables Completed<br/><br/><a href='index.jsp'><label class='btn btn-successful'>Open Data Extractor</label></a>");
        } catch (SQLException ex) {
            Logger.getLogger(recreateTables.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
