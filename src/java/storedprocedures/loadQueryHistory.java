/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storedprocedures;

import General.IdGenerator;
import database.dbConnweb;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author EKaunda
 */
public class loadQueryHistory extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
IdGenerator ig= new IdGenerator();
            
String month=""+ig.LastMonthDate();
            
String getlist="[{\"qry\":\"error\",\"queryname\":\"No Data\",\"qname\":\"No_Data_\"}]";
            


String checksp="SHOW PROCEDURE STATUS WHERE NAME like '%sp_anyb_TX_Curr_contacts%' and Db='KenyaEMR_datatools';";

            dbConnweb conn= new dbConnweb();
            
            conn.rs=conn.st.executeQuery(checksp);
            
            if(conn.rs.next()){
                
             getlist="[{\"qry\":\"call sp_anyb_TX_Curr('"+month+"');\",\"queryname\":\"Current ON ART with VL Results\",\"qname\":\"VL_KenyaEMR_\"},{\"qry\":\"call sp_anyb_Extended_RDQA_All_Patients();\",\"queryname\":\"Latest Greencard\",\"qname\":\"GreenCard_\"},{\"qry\":\"call sp_anyb_RDQA_All_Patients();\",\"queryname\":\"RDQA All Patients\",\"qname\":\"RDQA\"}]";
       
            
            }

out.println(getlist);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(loadQueryHistory.class.getName()).log(Level.SEVERE, null, ex);
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
