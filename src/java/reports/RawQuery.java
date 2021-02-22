/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;


import General.IdGenerator;
import database.dbConnweb;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;

/**
 *
 * @author GNyabuto
 */
public class RawQuery extends HttpServlet {
HttpSession session;
String query,query_original,message;
int row,errors;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        dbConnweb conn = new dbConnweb();
        session = request.getSession();
         String Reportname="Report";
         String periodname="";
        
       query_original = request.getParameter("query");
          if(request.getParameter("queryname")!=null){
       Reportname = request.getParameter("queryname");     
                                                     }
          
          if(request.getParameter("periodname")!=null){
            periodname = request.getParameter("periodname");
          }
       
      
       
       query = query_original.toLowerCase();
       
        IdGenerator ig= new IdGenerator();
       
       
       String file_name=getFacilityDetails(conn)+"_"+Reportname+"_As_at_"+periodname+"_Gen_"+ig.CreatedOnTime();
       
       row=errors=0;
       message = "";
       
        /* TODO output your page here. You may use following sample code. */
//______________________________________________________________________________________
//                       CREATE THE WORKSHEETS          
//______________________________________________________________________________________  
        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Daytona");
        font.setColor((short) 0000);
        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font2 = wb.createFont();
        font2.setFontName("Daytona");
        font2.setColor((short) 0000);
        font2.setFontHeightInPoints((short) 11);
        CellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style2.setBottomBorderColor(HSSFColor.GREEN.index);
        style2.setTopBorderColor(HSSFColor.GREEN.index);
        style2.setLeftBorderColor(HSSFColor.GREEN.index);
        style2.setRightBorderColor(HSSFColor.GREEN.index);
        

        XSSFCellStyle stborder = wb.createCellStyle();
        stborder.setBorderTop(XSSFCellStyle.BORDER_THIN);
        stborder.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        stborder.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        stborder.setBorderRight(XSSFCellStyle.BORDER_THIN);
        stborder.setBottomBorderColor(HSSFColor.GREEN.index);
        stborder.setTopBorderColor(HSSFColor.GREEN.index);
        stborder.setLeftBorderColor(HSSFColor.GREEN.index);
        stborder.setRightBorderColor(HSSFColor.GREEN.index);
        stborder.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        XSSFCellStyle stylex = wb.createCellStyle();
        stylex.setFillForegroundColor(HSSFColor.GREEN.index);
        stylex.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        stylex.setBorderTop(XSSFCellStyle.BORDER_THIN);
        stylex.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        stylex.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        stylex.setBorderRight(XSSFCellStyle.BORDER_THIN);
        stylex.setBottomBorderColor(HSSFColor.GREEN.index);
        stylex.setTopBorderColor(HSSFColor.GREEN.index);
        stylex.setLeftBorderColor(HSSFColor.GREEN.index);
        stylex.setRightBorderColor(HSSFColor.GREEN.index);
        
        stylex.setAlignment(XSSFCellStyle.ALIGN_LEFT);

        XSSFCellStyle stylesum = wb.createCellStyle();
        stylesum.setFillForegroundColor(HSSFColor.GREEN.index);
        stylesum.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        stylesum.setBorderTop(XSSFCellStyle.BORDER_THIN);
        stylesum.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        stylesum.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        stylesum.setBorderRight(XSSFCellStyle.BORDER_THIN);
        stylesum.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        
        stylesum.setBottomBorderColor(HSSFColor.GREEN.index);
        stylesum.setTopBorderColor(HSSFColor.GREEN.index);
        stylesum.setLeftBorderColor(HSSFColor.GREEN.index);
        stylesum.setRightBorderColor(HSSFColor.GREEN.index);

        XSSFFont fontx = wb.createFont();
        fontx.setColor(HSSFColor.BLACK.index);
        fontx.setFontName("Daytona");
        
        XSSFFont fontwhite = wb.createFont();
        fontwhite.setColor(HSSFColor.WHITE.index);
        fontwhite.setFontName("Daytona");
        fontwhite.setFontHeightInPoints((short)9);
                
        stylex.setFont(fontwhite);
        stylex.setWrapText(true);

        stylesum.setFont(fontx);
        stylesum.setWrapText(true);

        XSSFSheet Sheet = wb.createSheet(Reportname);
        
       // check query
       if(query.contains("insert") || query.contains("update") || query.contains("replace") || query.contains("into")  || query.contains("user") || query.contains("drop") || query.contains("truncate")){
           System.out.println("This query is not allowed");
           errors++;
           message="Your are running a wrong query. Any query attempting to change data is disabled";
       }
       else{
if(query.contains("select ") || query.contains("call ")){
  XSSFRow RowTitles = Sheet.createRow(row);  
    try {
        conn.rs = conn.st.executeQuery(query);
       String value;
        //get headers
        ResultSetMetaData metaData = conn.rs.getMetaData();
            int col_count = metaData.getColumnCount(); //number of column

            for (int i = 0; i < col_count; i++) {
                Sheet.setColumnWidth(col_count, 5000);
                value = metaData.getColumnLabel(i + 1);
                XSSFCell cell = RowTitles.createCell(i);
                if (isNumeric(value)) {
                    cell.setCellValue(Integer.parseInt(value));
                } else {
                    cell.setCellValue(value);
                }
                cell.setCellStyle(stylex);
            }
        
        
          while (conn.rs.next()) {
               // System.out.println("Name : " + conn.rs.getString(3));
                row++;
                //System.out.println("Row pos : .. " + row);
                XSSFRow RowData = Sheet.createRow(row);

                for (int i = 0; i < col_count; i++) { // read and output data
                    value = conn.rs.getString(i + 1);
                    XSSFCell cell = RowData.createCell(i);

                    if (isNumeric(value)) {
                        try{
                        cell.setCellValue(Integer.parseInt(value));
                        cell.setCellStyle(stborder); 
                        }
                        catch(NumberFormatException nfe){ // output it as a string
                        cell.setCellValue(value);
                        cell.setCellStyle(stborder);  
                        }
                    } 
                    else {
                        cell.setCellValue(value);
                        cell.setCellStyle(stborder);
                    }

                }

            }
          
          for (int e = 0; e < col_count; e++) {
                Sheet.autoSizeColumn(e);
            }

            Sheet.setDisplayGridlines(false);
        
        //get data
        
        System.out.println("query a success"); 
          
        
        
    } catch (SQLException ex) {
        Logger.getLogger(RawQuery.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Incomplete query. response is: "+ex);
        errors++;
        message="You are running an incomplete query. Check your syntax and try again.<br><br>Error message is : "+ex;
    }
    
    
} // seems legit query

else{//
//   query missing important info
System.out.println("Nothing to be done");
errors++;
message="There is nothing to be executed in this query. Review it and try executing again.";
}
       }
    
    if(errors==0){
       session.removeAttribute("errors");
      
       
     
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename="+file_name+".xlsx");
        OutputStream outStream = response.getOutputStream();
        outStream.write(outArray);
        outStream.flush();     
    }
    
    else{
     session.setAttribute("errors", message);
     session.setAttribute("query", query_original);
     response.sendRedirect("RawQuery.jsp");
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

    
    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private static String removeLast(String str, int num) {
        return str.substring(0, str.length() - num);
    }
    
    
    private String getFacilityDetails(dbConnweb conn){
    String facility_name="";
    try {
        
        String qry=" select * from kenyaemr_datatools.default_facility_info ";
        
        conn.rs=conn.st.executeQuery(qry);
        while(conn.rs.next())
        {
            
            facility_name=conn.rs.getString(2).replace("'","");
           // facility_name=conn.rs.getString(2).replace("'","")+"_"+conn.rs.getString(1);
            
        }
        
        
        
        
    } catch (SQLException ex) {
        Logger.getLogger(RawQuery.class.getName()).log(Level.SEVERE, null, ex);
        facility_name="Unknown_Facility";
    }
     return facility_name;
    }
   
}
