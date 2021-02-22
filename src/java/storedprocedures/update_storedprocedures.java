/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storedprocedures;

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
public class update_storedprocedures extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           
            dbConnweb conn = new dbConnweb();
            
            
            Create_sp_anyb_TX_Curr(conn);
            Create_sp_anyb_TX_Curr_contacts(conn);
            Create_sp_anyb_Extended_RDQA_All_Patients(conn);
            Create_sp_anyb_RDQA_All_Patients(conn);
            
            if(conn.rs!=null){conn.rs.close();}
            if(conn.rs1!=null){conn.rs1.close();}
            if(conn.st1!=null){conn.st.close();}
            
            
            
            out.println("Stored procedures created successfully");
        } catch (SQLException ex) {
            Logger.getLogger(update_storedprocedures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
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

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    public String Create_sp_anyb_RDQA_All_Patients(dbConnweb conn){
    
         String qry4="DROP procedure IF EXISTS `kenyaemr_datatools`.`sp_anyb_RDQA_All_Patients`;" ;
        String qry5="CREATE  PROCEDURE `kenyaemr_datatools`.`sp_anyb_RDQA_All_Patients`()\n" +
"BEGIN\n" +
"\n" +
"select distinct\n" +
"m.patient_id,\n" +
"case when m.unique_patient_no is null then 'Missing' Else m.unique_patient_no End as UniquePatientID ,\n" +
"Case When m.Gender Is Null Then 'Missing' Else m.Gender End As Gender,\n" +
"Case When m.DOB Is Null Then 'Missing' Else m.DOB End As DateOfBirth,\n" +
"\n" +
"Case When he.entry_point='' and he.transfer_in_date!=\"\" then 'Transfer_in'\n" +
"	 When he.entry_point='' and he.transfer_in_date=\"\" then 'Missing'\n" +
"     Else he.entry_point End As PatientSource,\n" +
"\n" +
"Case When he.transfer_in_date!=\"\" Then he.transfer_in_date \n" +
"     Else 'Missing' End As TransferInDate, \n" +
"     \n" +
"Case When he.date_confirmed_hiv_positive !=\"\" Then he.date_confirmed_hiv_positive Else 'Missing' End As DateConfirmedHIVPositive,\n" +
"\n" +
"Case When he.date_first_enrolled_in_care Is Not Null Then he.date_first_enrolled_in_care \n" +
"	When (he.date_first_enrolled_in_care is NULL and unique_patient_no is not null) then he.MinEnrollment Else 'Missing' End As DateEnrolledInHIVCare,\n" +
"\n" +
"Case When InitVisit.who_stage = 'WHO Stage1' Then 'I' When InitVisit.who_stage  = 'WHO Stage2' Then 'II' \n" +
"	 When InitVisit.who_stage  = 'WHO Stage3' Then 'III' When InitVisit.who_stage  = 'WHO Stage4' Then 'IV' Else 'Missing' End As FirstWHOStage,\n" +
"     \n" +
"Case When (startART.StartARTDate = '1900-01-01 00:00:00.000') Then 'Missing' \n" +
"	 When startART.StartARTDate   Is NULL Then 'Missing' ELSE startART.StartARTDate   End As ARTStartDate, \n" +
"\n" +
"Case When lwhostage.who_stage = 'WHO Stage1' Then 'I' When lwhostage.who_stage  = 'WHO Stage2' Then 'II' \n" +
"	 When lwhostage .who_stage = 'WHO Stage3' Then 'III' When lwhostage.who_stage= 'WHO Stage4' Then 'IV' Else 'Missing' End As MostRecentWHOStage,   \n" +
"\n" +
"Case When (ltbstatus.tb_status =\"\" OR ltbstatus.tb_status='TB Screening Not Done') Then 'Missing' \n" +
"	When  ltbstatus.tb_status Is Not Null Then ltbstatus.tb_status Else 'Missing' End As TBScreeningOutcome, \n" +
"\n" +
" Case When (startART.StartARTDate  is NULL And CurReg.regimen Is Null) Then 'N/A' \n" +
"	When (LastVisit.LastVisitDate Is Not Null And CurReg.regimen Is Null) Then 'Missing' Else CurReg.regimen End As CurrentARTRegimen,\n" +
"\n" +
"Case When (startART.StartARTDate Is Null) Then 'N/A' \n" +
"     When LastVisit.LastVisitDate  Is Not Null Then LastVisit.LastVisitDate  Else 'Missing' End As LastARTDate, \n" +
"\n" +
"Case When (CTX.ctxDispense Is Null) Then 'Missing' \n" +
"	 When (CTX.ctxDispense Is Not Null) Then CTX.ctxDispense else 'Missing' End As LastCTXDate, \n" +
"     \n" +
"Case When (IPT.IPTStartDate = '1900-01-01 00:00:00.000' Or IPT.IPTStartDate Is Null) Then 'Missing' \n" +
"	 When IPT.IPTStartDate Is Not Null Then IPT.IPTStartDate Else 'Missing' End As initialIPTDispenseDate,      \n" +
"\n" +
"Case When bCD4.FirstCD4Result Is Not Null Then bCD4.FirstCD4Result Else 'Missing' End As FirstCD4Result,\n" +
"Case When (bCD4.FirstCD4Date = '1900-01-01 00:00:00.000' Or bCD4.FirstCD4Date Is Null) Then 'Missing' When bCD4.FirstCD4Date Is Not Null Then bCD4.FirstCD4Date Else 'Missing' End As FirstCD4Date, \n" +
"\n" +
"Case When (startART.StartARTDate Is Null And ViralLoad.FirstVL Is Null) Then 'N/A' When ViralLoad.FirstVL Is Not Null Then ViralLoad.FirstVL Else 'Missing' End As FirstVLResult, \n" +
"Case When (startART.StartARTDate Is Null And ViralLoad.FirstVLDate Is Null) Then 'N/A' When (ViralLoad.FirstVLDate = '1900-01-01 00:00:00.000' Or ViralLoad.FirstVLDate Is Null) Then 'Missing' \n" +
"	 When ViralLoad.FirstVLDate Is Not Null Then ViralLoad.FirstVLDate Else 'Missing' End As FirstVLDate, \n" +
"\n" +
"Case When (startART.StartARTDate Is Null And lastVL.LastVL Is Null) Then 'N/A' When lastVL.LastVL Is Not Null Then lastVL.LastVL Else 'Missing' End As MostRecentVLResult, \n" +
"Case When (startART.StartARTDate Is Null And lastVL.LastVLDate Is Null) Then 'N/A' When (lastVL.LastVLDate = '1900-01-01 00:00:00.000' Or lastVL.LastVLDate Is Null) Then 'Missing' Else lastVL.LastVLDate End As MostRecentVLDate, \n" +
"    \n" +
"Case When (LastVisit.LastVisitDate Is Not Null) Then LastVisit.LastVisitDate Else 'Missing' End As lastvisitDate,\n" +
"\n" +
"Case When (lasts.next_appointment_date) Is Not Null Then lasts.next_appointment_date Else 'Missing' End As NextAppointmentDate,\n" +
"\n" +
"\n" +
"Case \n" +
"	 When ls.discontinuation_reason Is Not Null Then ls.discontinuation_reason\n" +
"	 When (ls.discontinuation_reason Is NULL and (datediff(curdate(),lasts.next_appointment_date) Between 1 And 90) \n" +
"			OR (ls.discontinuation_reason is NULL AND lasts.next_appointment_date is NULL and (datediff(curdate(),LastVisit.LastVisitDate) Between 1 And 90))) THEN 'Defaulter' \n" +
"	 When (ls.discontinuation_reason Is NULL and (datediff(curdate(),lasts.next_appointment_date) >90) \n" +
"			OR (ls.discontinuation_reason is NULL AND lasts.next_appointment_date is NULL and (datediff(curdate(),LastVisit.LastVisitDate)>90))) THEN 'Undocumented Lost'\n" +
"	 When (ls.discontinuation_reason Is NULL and (CTX.ctxDispense is NULL and startART.StartARTDate is NULL)) then 'No Pharmacy'\n" +
"	 When (ls.discontinuation_reason Is NULL and ce.Visit_Date Is Null) Then 'No Visits'\n" +
"   Else 'Active' End as PatientStatus,\n" +
"\n" +
"case when (TIMESTAMPDIFF(MONTH, startART.StartARTDate, LastVisit.LastVisitDate) > 6 and  ViralLoad.FirstVLDate  Is Null) then 'first' \n" +
"	 When ((lastVL.LastVL <=1000.00) AND TIMESTAMPDIFF(MONTH, lastVL.LastVLDate, Curdate()) > 12) then 'annual'\n" +
"	 When (lastVL.LastVL >1000.00 AND TIMESTAMPDIFF(MONTH, lastVL.LastVLDate, Curdate()) > 13) then 'retest'\n" +
"	 Else 'N/A' End as VLStatus,\n" +
"\n" +
" CASE\n" +
"    WHEN (he.date_confirmed_hiv_positive < m.DOB) THEN 'Confirmed+ve before Birth'\n" +
"	WHEN (he.date_first_enrolled_in_care < he.date_confirmed_hiv_positive) THEN 'Enrolled Before Confirmed+ve'\n" +
"    WHEN (startART.StartARTDate < he.date_first_enrolled_in_care) THEN    'Started ART Before Enrolled'\n" +
"    WHEN (LastVisit.LastVisitDate < he.date_first_enrolled_in_care) THEN 'Last ART Date before Enrollment' \n" +
"	WHEN (ViralLoad.FirstVLDate < startART.StartARTDate) THEN 'FirstVL before ART Start'\n" +
"	ELSE 'OK' END AS AccuracyIssue,\n" +
"\n" +
"case when (CTX.ctxDispense<LastVisit.LastVisitDate) then 'Pharmacy not updated' \n" +
"	 When (CTX.ctxDispense>LastVisit.LastVisitDate) then 'Last Visit not Updated'\n" +
"Else 'Ok' end as lastVisitStatus\n" +
"\n" +
"from patient_demographics m\n" +
"Left Join (select patient_id, min(visit_date)MinEnrollment,date_first_enrolled_in_care,transfer_in_date,entry_point, date_confirmed_hiv_positive from hiv_enrollment group by patient_id)he on he.patient_id=m.patient_id\n" +
"Left Join hiv_followup ce On m.Patient_id = ce.Patient_id\n" +
"\n" +
"Left Join (Select Max(ce.visit_date) As LastVisitDate, ce.patient_id From hiv_followup ce group by ce.patient_id) LastVisit On m.patient_id = LastVisit.patient_id\n" +
"\n" +
"Left JOIN (select distinct p.patient_id,InitialVisit,whostage who_stage from hiv_followup p\n" +
"		inner join (SELECT min(visit_date) As InitialVisit, patient_id , min(who_stage) whostage From hiv_followup group by patient_id) h\n" +
"					on h.patient_id=p.patient_id and p.visit_date=h.InitialVisit  where who_stage !=\"\") InitVisit on m.patient_id=InitVisit.patient_id\n" +
"\n" +
"Left JOIN (select distinct p.patient_id,LastVisit,whostage who_stage from hiv_followup p\n" +
"		inner join (SELECT max(visit_date) As LastVisit, patient_id, min(who_stage)whostage From hiv_followup group by patient_id) h\n" +
"					on h.patient_id=p.patient_id and p.visit_date=h.LastVisit where who_stage !=\"\") lwhostage on m.patient_id=lwhostage.patient_id\n" +
"\n" +
"Left JOIN (select distinct p.patient_id,LastVisit,tb_status from hiv_followup p\n" +
"		inner join (SELECT max(visit_date) As LastVisit, patient_id From hiv_followup group by patient_id) h\n" +
"					on h.patient_id=p.patient_id and p.visit_date=h.LastVisit where tb_status !=\"\") ltbstatus on m.patient_id=ltbstatus.patient_id\n" +
"\n" +
"Left JOIN (select distinct p.patient_id,LastVisit,max(next_appointment_date) next_appointment_date from hiv_followup p\n" +
"		inner join (SELECT max(visit_date) As LastVisit, patient_id From hiv_followup group by patient_id) h\n" +
"					on h.patient_id=p.patient_id and p.visit_date=h.LastVisit where next_appointment_date!=\"\" group by patient_id) lasts on m.patient_id=lasts.patient_id\n" +
"\n" +
"Left Join (Select min(de.date_started) As startARTDate, de.patient_id From drug_event de group by de.patient_id) startART On m.patient_id = startART.patient_id\n" +
"\n" +
"Left Join (Select pel.patient_id,pel.regimen From drug_event pel where (discontinued is NULL or discontinued!=1) group by pel.patient_id) CurReg On m.patient_id = CurReg.patient_id\n" +
"\n" +
"Left Join (select patient_id ,max(visit_date) As ctxDispense from pharmacy_extract where drug_name like 'dapson%' or drug_name like 'sulf%' group by patient_id) CTX On m.patient_id = CTX.patient_id\n" +
"\n" +
"Left Join (select min(IPT.date_collected_ipt) IPTStartDate, IPT.patient_id from ipt_followup IPT group by IPT.patient_id) IPT on m.patient_id=IPT.patient_id\n" +
"\n" +
"Left Join (select distinct p.patient_id,FirstCD4Date,FirstCD4Result from laboratory_extract p\n" +
"			 inner join (SELECT min(visit_date) As FirstCD4Date, patient_id,min(test_result)FirstCD4Result From laboratory_extract where lab_test like 'CD4%' group by patient_id) h\n" +
"				on h.patient_id=p.patient_id and p.visit_date=h.FirstCD4Date where lab_test like '%CD4%')bCD4  on m.patient_id=bCD4.patient_id\n" +
"\n" +
"Left Join (select distinct p.patient_id,FirstVLDate,FirstVL from laboratory_extract p\n" +
"			 inner join (SELECT min(visit_date) As FirstVLDate, patient_id,min(test_result)FirstVL From laboratory_extract where lab_test like '%VIRAL%' group by patient_id) h\n" +
"				on h.patient_id=p.patient_id and p.visit_date=h.FirstVLDate where lab_test like '%VIRAL%')ViralLoad  on m.patient_id=ViralLoad.patient_id\n" +
"\n" +
"Left Join (select distinct p.patient_id,lastVLDate,LastVL from laboratory_extract p\n" +
"		   inner join (SELECT max(visit_date) As lastVLDate, patient_id,max(test_result)LastVL From laboratory_extract where lab_test like '%VIRAL%' group by patient_id) h\n" +
"		   on h.patient_id=p.patient_id and p.visit_date=h.lastVLDate where lab_test like '%VIRAL%' )lastVL  on m.patient_id=lastVL.patient_id\n" +
"\n" +
"Left Join (select distinct p.patient_id, exitDate,discontinuation_reason from patient_program_discontinuation p\n" +
"				inner join (SELECT max(visit_date) As exitDate, patient_id From patient_program_discontinuation where program_name ='HIV' group by patient_id)h\n" +
"		   on h.patient_id=p.patient_id and p.visit_date=h.exitDate)ls on m.patient_id = ls.patient_id\n" +
"\n" +
"where   unique_patient_no is not null\n" +
"order by patientStatus;\n" +
"\n" +
"\n" +
"\n" +
"END";
    
    try {
            conn.st.executeUpdate(qry4);
            conn.st.executeUpdate(qry5);
            System.out.println("Query Created successfully");
        } catch (SQLException ex) 
        {
            Logger.getLogger(update_storedprocedures.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error Creating SP");
        } 
    
    
    
    return "Complete";
    }
    
    public String Create_sp_anyb_Extended_RDQA_All_Patients(dbConnweb conn){
    
        String qry2="DROP procedure IF EXISTS `kenyaemr_datatools`.`sp_anyb_Extended_RDQA_All_Patients`;" ;
        String qry3="CREATE  PROCEDURE `kenyaemr_datatools`.`sp_anyb_Extended_RDQA_All_Patients`()\n" +
"BEGIN\n" +
"\n" +
"/* This query helps in retriving data from greencard and dqa run against kenyaemr_datatools\n" +
"written by @LAVATSA\n" +
"*/\n" +
"\n" +
"SELECT *,\n" +
"CASE\n" +
"	WHEN DWHValidation.HIV_DiagnosisDate_Validation = 'Invalid' THEN 'Invalid'\n" +
"    WHEN DWHValidation.HIV_EnrollmentDate_Validation = 'Invalid' THEN 'Invalid'\n" +
"    WHEN DWHValidation.HIV_ARTStartDate_Validation = 'Invalid' THEN 'Invalid'\n" +
"    WHEN DWHValidation.HIV_FirstVLDate_Validation = 'Invalid' THEN 'Invalid'\n" +
"    WHEN DWHValidation.HIV_LastVLDate_Validation = 'Invalid' THEN 'Invalid'\n" +
"    WHEN DWHValidation.date_confirmed_hiv_positive = 'Missing' THEN 'Invalid'\n" +
"    WHEN DWHValidation.enroll_date = 'Missing' THEN 'Invalid'\n" +
"    #WHEN DWHValidation.ARTStartDate = 'Missing' THEN 'Invalid'\n" +
"	WHEN datediff(CURDATE(), STR_TO_DATE(DWHValidation.ARTStartDate,'%d-%b-%Y')) > 185 and  DWHValidation.First_VL_Date = 'Missing' THEN 'Invalid'\n" +
"    ELSE 'Valid'\n" +
"END as RecordValid\n" +
"FROM (\n" +
"SELECT *, \n" +
"round(DATEDIFF(CURRENT_DATE, STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y'))/365.25, 1) as Years_on_ART, \n" +
"ifnull(DATEDIFF(CURRENT_DATE, STR_TO_DATE(TX_CURR.Last_VL_Date,'%d-%b-%Y')), 'Missing') as days_since_lastVL,\n" +
"CASE \n" +
"	WHEN STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') <= STR_TO_DATE(TX_CURR.DOB,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') < '01/01/1984' THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') > STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') > now() THEN 'Invalid'\n" +
"    ELSE 'Valid'\n" +
"END AS HIV_DiagnosisDate_Validation,\n" +
"CASE \n" +
"	WHEN STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') <= STR_TO_DATE(TX_CURR.DOB,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') < '01/01/2000' THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') > now() THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') THEN 'Invalid'\n" +
"    ELSE 'Valid'\n" +
"END AS HIV_EnrollmentDate_Validation,\n" +
"CASE \n" +
"	WHEN STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y') <= STR_TO_DATE(TX_CURR.DOB,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y') < '01/01/2004' THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y') > now() THEN 'Invalid'\n" +
"    ELSE 'Valid'\n" +
"END AS HIV_ARTStartDate_Validation,\n" +
"CASE \n" +
"	WHEN STR_TO_DATE(TX_CURR.First_VL_date,'%d-%b-%Y') <= STR_TO_DATE(TX_CURR.DOB,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.First_VL_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.First_VL_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.First_VL_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN datediff(CURDATE(), STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y')) > 185 and  TX_CURR.First_VL_Date = 'Missing' THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.First_VL_date,'%d-%b-%Y') > now() THEN 'Invalid'\n" +
"    ELSE 'Valid'\n" +
"END AS HIV_FirstVLDate_Validation,\n" +
"CASE \n" +
"	WHEN STR_TO_DATE(TX_CURR.Last_VL_date,'%d-%b-%Y') <= STR_TO_DATE(TX_CURR.DOB,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.Last_VL_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.date_confirmed_hiv_positive,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.Last_VL_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.enroll_date,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.Last_VL_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.ARTStartDate,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.Last_VL_date,'%d-%b-%Y') < STR_TO_DATE(TX_CURR.First_VL_date,'%d-%b-%Y') THEN 'Invalid'\n" +
"    WHEN STR_TO_DATE(TX_CURR.Last_VL_date,'%d-%b-%Y') > now() THEN 'Invalid'\n" +
"    ELSE 'Valid'\n" +
"END AS HIV_LastVLDate_Validation\n" +
"FROM (\n" +
"	SELECT (select siteCode from default_facility_info) MFL, (select Facilityname from default_facility_info) Facility,\n" +
"	C1.unique_patient_no, round(DATEDIFF(LAST_DAY(now() - INTERVAL 1 MONTH), A.dob)/365.25, 2) AS ageInYears, A.Gender, ifnull(date_format(A.dob,'%d-%b-%Y'), 'Missing') DOB, \n" +
"    ifnull(date_format(E.date_confirmed_hiv_positive,'%d-%b-%Y'), 'Missing') date_confirmed_hiv_positive,\n" +
"    ifnull(date_format(A.enroll_date,'%d-%b-%Y'), 'Missing') enroll_date,\n" +
"	CASE \n" +
"		WHEN (select min(date_started_art_at_transferring_facility) from hiv_enrollment where patient_id = A.patient_id) IS NOT NULL\n" +
"				AND (select min(date_started_art_at_transferring_facility) from hiv_enrollment where patient_id = A.patient_id) < \n" +
"				(select min(date_started) from drug_event where patient_id = A.patient_id)\n" +
"		THEN ifnull(date_format((select min(date_started_art_at_transferring_facility) from hiv_enrollment where patient_id = A.patient_id),'%d-%b-%Y'),'Missing')\n" +
"		ELSE ifnull(date_format((select min(date_started) from drug_event where patient_id = A.patient_id),'%d-%b-%Y'),'Missing') \n" +
"	END AS ARTStartDate,\n" +
"	(select who_stage from kenyaemr_datatools.hiv_followup where patient_id = A.patient_id order by visit_date asc LIMIT 1) AS baselineWHO,\n" +
"    (select test_result from laboratory_extract where patient_id = A.patient_id and lab_test = 'CD4 Count' order by visit_date asc LIMIT 1) baselineCD4,\n" +
"    CASE\n" +
"		WHEN H.test_result IS NOT NULL THEN H.test_result\n" +
"		WHEN DATEDIFF(LAST_DAY(now() - INTERVAL 1 MONTH), \n" +
"			(\n" +
"				CASE \n" +
"					WHEN (select min(date_started_art_at_transferring_facility) from hiv_enrollment where patient_id = A.patient_id) IS NOT NULL\n" +
"							AND (select min(date_started_art_at_transferring_facility) from hiv_enrollment where patient_id = A.patient_id) < \n" +
"							(select min(date_started) from drug_event where patient_id = A.patient_id)\n" +
"					THEN ifnull((select min(date_started_art_at_transferring_facility) from hiv_enrollment where patient_id = A.patient_id),'Missing')\n" +
"					ELSE ifnull((select min(date_started) from drug_event where patient_id = A.patient_id),'Missing') \n" +
"				END #ARTSTARTDATE\n" +
"			)\n" +
"		) < 183 THEN 'Not Eligible'\n" +
"        ELSE ifnull(H.test_result,'Missing')\n" +
"	END First_VL_Result,\n" +
"    ifnull(date_format(H.visit_date,'%d-%b-%Y'),'Missing') First_VL_Date,\n" +
"    ifnull(B.test_result,'Missing') Last_VL_Result, ifnull(date_format(B.visit_date,'%d-%b-%Y'),'Missing') Last_VL_Date,\n" +
"    if(D.person_present is null or D.person_present='', 'Missing', D.person_present) person_present, D.weight, D.height, \n" +
"	CASE \n" +
"		WHEN D.weight/ ((D.height/100) * (D.height/100)) < 18.5 THEN 'Underweight'\n" +
"		WHEN D.weight/ ((D.height/100) * (D.height/100)) between 18.5 and 25 THEN 'Normal'\n" +
"		WHEN D.weight/ ((D.height/100) * (D.height/100)) > 25 THEN 'Overweight'\n" +
"	END as nutritional_status, #D.nutritional_status,\n" +
"     D.temperature, D.muac,\n" +
"    D.population_type, D.key_population_type, D.who_stage AS currentWHO, D.presenting_complaints,\n" +
"    D.on_anti_tb_drugs, D.on_ipt, D.ever_on_ipt, ifnull(date_format(G.visit_date,'%d-%b-%Y'),'Missing') IPTStartDate,\n" +
"    CASE \n" +
"		WHEN F.outcome = 1267 THEN 'Completed'\n" +
"        WHEN F.outcome = 5240 THEN 'Lost to followup'\n" +
"        WHEN F.outcome = 159836 THEN 'Discontinue'\n" +
"        WHEN F.outcome = 160034 THEN 'Died'\n" +
"        WHEN F.outcome = 159492 THEN 'Transferred Out'\n" +
"        WHEN F.outcome = 112141 THEN 'Tuberculosis'\n" +
"        WHEN F.outcome = 102 THEN 'Drug Toxicity'\n" +
"        ELSE 'Missing'\n" +
"	END AS IPTOutcome, \n" +
"    ifnull(date_format(F.visit_date,'%d-%b-%Y'),'Missing') IPTOutcomeDate,\n" +
"    IF(D.tb_status IS NULL or D.tb_status = '', 'Not done', D.tb_status) TB_Status, D.has_known_allergies, D.has_chronic_illnesses_cormobidities, D.has_adverse_drug_reaction,\n" +
"    D.pregnancy_status, D.wants_pregnancy, D.family_planning_status, D.family_planning_method, D.ctx_dispensed, ifnull(C.regimen, 'Missing') current_regimen, C.regimen_line, D.arv_adherence, D.cacx_screening,\n" +
"	CASE \n" +
"		WHEN C.regimen IS NULL THEN 'Missing'\n" +
"        WHEN I.value_datetime > A.latest_vis_date and I.value_datetime < latest_tca THEN \n" +
"			CASE WHEN round(datediff(I.value_datetime, A.latest_vis_date)/30) = 0 THEN 1 ELSE round(datediff(I.value_datetime, A.latest_vis_date)/30) END\n" +
"        ELSE \n" +
"			CASE WHEN round(datediff(latest_tca, A.latest_vis_date)/30) = 0 THEN 1 ELSE round(datediff(latest_tca, A.latest_vis_date)/30) END\n" +
"		#WHEN round(datediff(latest_tca, A.latest_vis_date)/30) = 0 THEN 1 \n" +
"        #ELSE round(datediff(latest_tca, A.latest_vis_date)/30) \n" +
"	END AS MMD,\n" +
"	D.stability, \n" +
"    date_format(D.visit_date,'%d-%b-%Y') last_visit_date, date_format(D.next_appointment_date,'%d-%b-%Y') next_appointment_date, \n" +
"    round(datediff(D.next_appointment_date, D.visit_date)/30) App_Months, D.next_appointment_reason,\n" +
"    CASE WHEN I.value_datetime > D.visit_date THEN date_format(I.value_datetime,'%d-%b-%Y') ELSE '' END AS refill_date, D.differentiated_care,\n" +
"    CASE WHEN MCH.program IS NOT NULL THEN MCH.program ELSE 'CCC' END Program, MCH.MCH_Date_enrolled, MCH.LMP, MCH.EDD\n" +
"	FROM current_in_care A \n" +
"	LEFT JOIN \n" +
"	(select * from (select * from laboratory_extract where lab_test = 'HIV VIRAL LOAD' order by visit_date desc) X group by X.patient_id) B on A.patient_id = B.patient_id\n" +
"	\n" +
"    LEFT JOIN\n" +
"	(select * from (select unique_patient_no,patient_id from patient_demographics  ) Y1 group by Y1.patient_id) C1 on A.patient_id = C1.patient_id\n" +
"    LEFT JOIN\n" +
"	(select * from (select * from drug_event where program = 'HIV' order by visit_date desc) Y group by Y.patient_id) C on A.patient_id = C.patient_id\n" +
"	LEFT JOIN\n" +
"	(select * from (select * from hiv_followup where person_present <> '' order by visit_date desc) Z group by Z.patient_id) D ON A.patient_id = D.patient_id\n" +
"	LEFT JOIN\n" +
"	(select * from (select * from hiv_enrollment order by visit_date asc) W group by W.patient_id) E on A.patient_id = E.patient_id\n" +
"    LEFT JOIN\n" +
"    (select * from (select * from kenyaemr_etl.etl_ipt_outcome order by visit_date desc) V group by V.patient_id) F on A.patient_id = F.patient_id\n" +
"    LEFT JOIN\n" +
"    (select * from (select * from kenyaemr_etl.etl_ipt_initiation order by visit_date desc) U group by U.patient_id) G on A.patient_id = G.patient_id\n" +
"    LEFT JOIN \n" +
"	(select * from (select * from laboratory_extract where lab_test = 'HIV VIRAL LOAD' order by visit_date asc) T group by T.patient_id) H on A.patient_id = H.patient_id\n" +
"    LEFT JOIN\n" +
"    (select * from (select person_id, value_datetime from openmrs.obs where concept_id=162549 order by value_datetime desc) S group by S.person_id) I on A.patient_id = I.person_id\n" +
"    LEFT JOIN\n" +
"    (\n" +
"		SELECT pp.patient_id, date_format(pp.date_enrolled,'%d-%b-%Y') MCH_Date_enrolled, date_format(mchEnrol.lmp,'%d-%b-%Y') LMP, date_format(date_add(mchEnrol.lmp, INTERVAL 280 DAY),'%d-%b-%Y') EDD, 'MCH' program \n" +
"		FROM openmrs.patient_program pp inner join kenyaemr_datatools.mch_enrollment mchEnrol on pp.patient_id = mchEnrol.patient_id\n" +
"		where program_id=4 #MCH Mother Services\n" +
"		and (date_completed >= LAST_DAY(now() - INTERVAL 1 MONTH) OR date_completed is null) \n" +
"		and voided=0\n" +
"    ) MCH ON A.patient_id = MCH.patient_id\n" +
"	) TX_CURR) DWHValidation ;END";
    
    try {
            conn.st.executeUpdate(qry2);
            conn.st.executeUpdate(qry3);
            System.out.println("Extended Query Created successfully");
        } catch (SQLException ex) 
        {
            Logger.getLogger(update_storedprocedures.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error Creating SP");
        }
    
    return "Complete";
    }
    public String Create_sp_anyb_TX_Curr(dbConnweb conn){
    
        
    String qry="DROP procedure IF EXISTS `kenyaemr_datatools`.`sp_anyb_TX_Curr`;" ;


String qry1="CREATE  PROCEDURE `kenyaemr_datatools`.`sp_anyb_TX_Curr`(IN End_Date varchar(255))\n" +
"BEGIN\n" +
"\n" +
"\n" +
"\n" +
"SET @endDate = End_Date;\n" +
"\n" +
"SET @startdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 12 MONTH),'%Y-%m-%d');\n" +
"SET @lastquarterdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 3 MONTH),'%Y-%m-%d');\n" +
"SET @last6monthsdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 6 MONTH),'%Y-%m-%d');\n" +
"SET @last24monthsdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 24 MONTH),'%Y-%m-%d');\n" +
"SET @ym=substring(replace(@endDate,'-',''),1,6);\n" +
"\n" +
"    \n" +
"    #_____________________________________________________________________________________________________________________________\n" +
"\n" +
"Select \n" +
"vl_kenyaemr_id\n" +
",AgeYrs\n" +
",Sex\n" +
",cccno\n" +
",HIV_Enrollment_Date\n" +
",ART_Start_Date\n" +
",Last_VL\n" +
",Last_VL_Date\n" +
",Facility_Name\n" +
",MFL_Code\n" +
",Justification\n" +
",PMTCT\n" +
",Yearmonth\n" +
",replace(siku_ya_mwisho,' 00:00:00','') as Last_Visit_Date\n" +
",replace(Next_appointment_date,' 00:00:00','') as Next_appointment_date \n" +
",Indicator\n" +
",regimen_name\n" +
", regimen_line\n" +
" , IFNULL(uzito,'') as 'Weight'\n" +
" ,patient_stable\n" +
" ,Differentiated_care\n" +
" ,poptype as Population_Type\n" +
" \n" +
", key_population_type\n" +
", pregnancy_status \n" +
", expected_delivery_date\n" +
"\n" +
", family_planning_status\n" +
", family_planning_method\n" +
", Screened_for_Cervical_Cancer\n" +
", VisitScheduled\n" +
" ,cacx_screening_6months as cxca_Screened_last6Months\n" +
" ,pregnant_last12months\n" +
"from (\n" +
"select \n" +
"Concat(siteCode,'_',@ym,'_',m.patient_id) as vl_kenyaemr_id,\n" +
"FLOOR(IFNULL(DATEDIFF(@enddate, STR_TO_DATE(m.DOB, '%Y-%m-%d'))/365,0)) As AgeYrs,\n" +
"Case When m.Gender Is Null Then 'Missing' Else m.Gender End As Sex,\n" +
"case when m.unique_patient_no is null then 'Missing' Else m.unique_patient_no End as cccno ,\n" +
"Case When he.date_confirmed_hiv_positive !='' Then he.date_confirmed_hiv_positive Else 'Missing' End As HIV_Enrollment_Date,\n" +
"Case When (startART.StartARTDate = '1900-01-01 00:00:00.000') Then 'Missing' \n" +
"When startART.StartARTDate   Is NULL Then 'Missing' ELSE startART.StartARTDate   End As ART_Start_Date, \n" +
"Case  When lastVL.LastVL Is Not Null Then lastVL.LastVL Else 'Missing' End As Last_VL,    \n" +
"Case  When (lastVL.LastVLDate = '1900-01-01 00:00:00.000' Or lastVL.LastVLDate Is Null) Then 'Missing' Else lastVL.LastVLDate End As Last_VL_Date, \n" +
"Case  When (lastVL.LastVLDate = '1900-01-01 00:00:00.000' Or lastVL.LastVLDate Is Null) Then 'Missing_VL' Else 'Current_VL' End As Indicator,\n" +
"default_facility_info.FacilityName as Facility_Name,\n" +
"default_facility_info.siteCode as   MFL_Code\n" +
" ,\n" +
" case \n" +
"when order_reason_ ='843' then  'Confirmation of Treatment Failure (Repeat VL)'\n" +
"when order_reason_='1434' then 'Pregnant Mother'\n" +
"when order_reason_='162080' then 'Baseline'\n" +
"when order_reason_='162081' then 'Routine VL' #Follow Up\n" +
"when order_reason_='1259' then 'Single Drug Substitution'\n" +
"when order_reason_='159882' then 'Breast Feeding Mothers'\n" +
"when order_reason_='163523' then 'Clinical Failure'\n" +
"when order_reason_='161236'  then 'Routine VL'\n" +
"else  'Routine VL'\n" +
" end as Justification\n" +
" ,\n" +
"Case\n" +
"when order_reason_='1434' then 'Pregnant'\n" +
"when order_reason_='159882' then 'Breast Feeding'\n" +
"else  'No Data'\n" +
"end as PMTCT\n" +
"\n" +
" ,@ym as Yearmonth\n" +
" , ce.nextapp as next_appointment_date, discontinuation_reason, exitDate\n" +
" , CurReg.rname as regimen_name\n" +
", CurReg.rline as regimen_line\n" +
",uzito\n" +
",siku_ya_mwisho\n" +
" ,ifnull(stability,'') as Patient_stable\n" +
" ,ifnull(dsd,'') as Differentiated_care\n" +
",ifnull(poptype,'General Population') as poptype,\n" +
"ifnull(key_population_type,'') as key_population_type,\n" +
"CASE when m.Gender='F' then ifnull(pregnancy_status,'') else 'NA' end as  pregnancy_status,\n" +
"CASE when m.Gender='F' then ifnull(expected_delivery_date,'') else 'NA' end as  expected_delivery_date,\n" +
"\n" +
"ifnull(family_planning_status,'') as family_planning_status,\n" +
"ifnull(family_planning_method,'') as family_planning_method,\n" +
"ifnull(cacx_screening,'') as Screened_for_Cervical_Cancer, \n" +
"ifnull(visit_scheduled,'') as VisitScheduled,\n" +
"CASE when m.Gender='F' then ifnull(cacx_screening_6months,'No') else 'NA' end as  cacx_screening_6months,\n" +
"CASE when m.Gender='F' then ifnull(prenant_las12months,'No') else 'NA' end as  pregnant_last12months\n" +
"\n" +
"from\n" +
" \n" +
" patient_demographics m\n" +
" \n" +
"\n" +
"Left Join ( select patient_id, min(visit_date)MinEnrollment\n" +
", date_first_enrolled_in_care\n" +
", transfer_in_date\n" +
", entry_point\n" +
", date_confirmed_hiv_positive \n" +
"\n" +
"from hiv_enrollment group by patient_id ) he \n" +
"on he.patient_id=m.patient_id\n" +
"\n" +
"/**______Current ON ART______**/\n" +
" Join (\n" +
"/***********************************************************************************************************/\n" +
"select \n" +
"    \n" +
"    fup.visit_date as initial_visit_date,\n" +
"    fup.patient_id as pid,    \n" +
"	greatest(max(fup.visit_date), ifnull(max(d.visit_date),'0000-00-00')) as siku_ya_mwisho,\n" +
"	greatest(mid(max(concat(fup.visit_date,fup.next_appointment_date)),11), ifnull(max(d.visit_date),'0000-00-00')) as nextapp,\n" +
"	mid(max(concat(fup.visit_date,fup.stability)),11) as stability,\n" +
"	d.patient_id as disc_patient,\n" +
"	d.effective_disc_date as effective_disc_date,\n" +
"	max(d.visit_date) as date_discontinued,\n" +
"    mid(max(concat(fup.visit_date,fup.differentiated_care)),11) as dsd\n" +
"    \n" +
"	from hiv_followup fup\n" +
"		join patient_demographics p on p.patient_id=fup.patient_id	\n" +
"	\n" +
"		left outer JOIN\n" +
"		(select patient_id, \n" +
"        coalesce(date(effective_discontinuation_date),visit_date) visit_date,\n" +
"        max(date(effective_discontinuation_date)) as effective_disc_date \n" +
"        from kenyaemr_etl.etl_patient_program_discontinuation\n" +
"		where date(visit_date) <= date(@endDate) and program_name='HIV'\n" +
"		group by patient_id\n" +
"		) d on d.patient_id = fup.patient_id\n" +
"	where fup.visit_date <= date(@endDate)\n" +
"	group by pid\n" +
"	having  (\n" +
"		    ((timestampdiff(DAY,date(nextapp),date(@endDate)) <= 30 or timestampdiff(DAY,date(nextapp),date(curdate())) <= 30) \n" +
"        and (date(d.effective_disc_date) > date(@endDate) or d.effective_disc_date is null))\n" +
"		and (date(siku_ya_mwisho) >= date(date_discontinued) or date(nextapp) >= date(date_discontinued) or disc_patient is null)\n" +
"			)\n" +
"\n" +
"/***********************************************************************************************************/\n" +
"\n" +
"\n" +
") ce on ce.pid=m.patient_id\n" +
"\n" +
"\n" +
"Left Join (Select min(de.date_started) As startARTDate, de.patient_id From drug_event de group by de.patient_id) startART On m.patient_id = startART.patient_id\n" +
"\n" +
"\n" +
"/**______Regimen Line______**/\n" +
"\n" +
"Left Join (Select pel.patient_id,pel.regimen_name as rname, pel.regimen_line as rline From drug_event pel where (discontinued is NULL or discontinued!=1) group by pel.patient_id) CurReg On m.patient_id = CurReg.patient_id\n" +
"\n" +
"/**______Triage______**/\n" +
"\n" +
"\n" +
"Left Join (Select tri.patient_id as tr_pid, weight as uzito From triage tri \n" +
"      join ( select Max(visit_date) as tr_tk , patient_id from triage group by patient_id\n" +
"           ) max_tri on max_tri.patient_id=tri.patient_id\n" +
"        \n" +
" group by tri.patient_id) Curuzito On m.patient_id = Curuzito.tr_pid\n" +
"\n" +
"/**______Viral Load______**/\n" +
"\n" +
"\n" +
" left Join (\n" +
" select  \n" +
"p.patient_id,\n" +
"lastVLDate,\n" +
"max(test_result)  as LastVL\n" +
",oid,\n" +
"ores as order_reason_\n" +
"\n" +
"from laboratory_extract p\n" +
"join \n" +
"(\n" +
" select uuid, order_id as oid\n" +
"from kenyaemr_etl.etl_laboratory_extract  \n" +
") etl \n" +
"on etl.uuid=p.uuid\n" +
"\n" +
"\n" +
"left join\n" +
"\n" +
"(\n" +
"SELECT order_id, order_reason as ores, patient_id FROM openmrs.orders\n" +
") vl_reason on vl_reason.order_id=etl.oid\n" +
"\n" +
"   join \n" +
"  (\n" +
"  SELECT \n" +
"  \n" +
"  max(visit_date) As lastVLDate, \n" +
"  \n" +
"  patient_id\n" +
"   \n" +
"  From laboratory_extract where (lab_test like '%VIRAL%') and (visit_date between @startdate and @enddate )\n" +
"  group by patient_id\n" +
"  \n" +
"  ) h\n" +
"		   on h.patient_id=p.patient_id and p.visit_date=h.lastVLDate where lab_test like '%VIRAL%'\n" +
"          \n" +
"          \n" +
"           group by p.patient_id ) lastVL  \n" +
"           on m.patient_id=lastVL.patient_id\n" +
"           \n" +
"   /*_____________Population Type and other Green Card Items__________*/        \n" +
"Left Join (\n" +
"Select \n" +
"gcf.patient_id as poptype_patient_id,\n" +
"population_type as poptype ,\n" +
"key_population_type,\n" +
"pregnancy_status,\n" +
"expected_delivery_date,\n" +
"\n" +
"family_planning_status,\n" +
"family_planning_method,\n" +
"cacx_screening,\n" +
"visit_scheduled\n" +
"\n" +
"\n" +
"From hiv_followup gcf  \n" +
"join ( select patient_id as poptype_patient_id,  \n" +
"max(visit_date) as pop_type_maxvdate from hiv_followup where date(visit_date)<=date(@EndDate) group by patient_id) max_poptype \n" +
"on max_poptype.poptype_patient_id=poptype_patient_id and pop_type_maxvdate=gcf.visit_date\n" +
"group by gcf.patient_id\n" +
") Curpop\n" +
"\n" +
" \n" +
"On m.patient_id = Curpop.poptype_patient_id\n" +
"\n" +
"\n" +
"/*________________________Check if patient screened for cancer in the last 6 months__________________________________________*/\n" +
"   \n" +
"Left Join \n" +
"(\n" +
"select \n" +
"patient_id as cxca_patient_id, \n" +
"'Yes' as cacx_screening_6months \n" +
"from hiv_followup where \n" +
"( visit_date between @last6monthsdate and @EndDate) and cacx_screening IN ('NEGATIVE','POSITIVE') group by cxca_patient_id   \n" +
"\n" +
") cxca\n" +
"\n" +
" \n" +
"On m.patient_id = cxca.cxca_patient_id\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"/*________________________Check if patient pregnant in the last 12 months__________________________________________*/\n" +
"   \n" +
"Left Join \n" +
"(\n" +
"select \n" +
"patient_id as pmtct_patient_id, \n" +
"'Yes' as prenant_las12months \n" +
"from hiv_followup where \n" +
"( visit_date between @startdate and @EndDate) and pregnancy_status IN ('Yes') group by pmtct_patient_id   \n" +
"\n" +
") pmtct\n" +
"\n" +
" \n" +
"On m.patient_id = pmtct.pmtct_patient_id\n" +
"\n" +
"\n" +
"     \n" +
" /**______discontinuation______**/\n" +
"\n" +
"/**___exclude Patients who have been discontinued in the last quarter _________________**/\n" +
"\n" +
"Left Join ( select distinct p.patient_id, \n" +
"exitDate,\n" +
"discontinuation_reason \n" +
"from patient_program_discontinuation p\n" +
"inner join \n" +
"( SELECT max(visit_date) As exitDate, patient_id From patient_program_discontinuation\n" +
" where program_name ='HIV' group by patient_id ) h\n" +
"		   on h.patient_id=p.patient_id and p.visit_date=h.exitDate \n" +
"           \n" +
"           where DATE_FORMAT(exitDate,'%Y-%m-%d') >=@lastquarterdate\n" +
"           ) ls \n" +
"           on m.patient_id = ls.patient_id\n" +
"  \n" +
"  cross join default_facility_info \n" +
"\n" +
"   order by Last_VL_Date\n" +
"   \n" +
"   \n" +
"   \n" +
"   \n" +
"\n" +
") as dt \n" +
"where 1=1 \n" +
"\n" +
"\n" +
"\n" +
"/** and (Last_VL_Date not in ('Missing')  ) **/\n" +
"\n" +
"group by vl_kenyaemr_id;"
        + "END";
    
        try {
            conn.st.executeUpdate(qry);
            conn.st.executeUpdate(qry1);
            System.out.println("TXCurr Query Created successfully");
        } catch (SQLException ex) 
        {
            Logger.getLogger(update_storedprocedures.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error Creating SP");
        }
    
    return "Complete";
    }
    
    public String Create_sp_anyb_TX_Curr_contacts(dbConnweb conn){
    
        
    String qry="DROP procedure IF EXISTS `kenyaemr_datatools`.`sp_anyb_TX_Curr_contacts`;" ;


String qry1="CREATE  PROCEDURE `kenyaemr_datatools`.`sp_anyb_TX_Curr_contacts`(IN End_Date varchar(255))\n" +
"BEGIN\n" +
"\n" +
"\n" +
"\n" +
"SET @endDate = End_Date;\n" +
"\n" +
"SET @startdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 12 MONTH),'%Y-%m-%d');\n" +
"SET @lastquarterdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 3 MONTH),'%Y-%m-%d');\n" +
"SET @last6monthsdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 6 MONTH),'%Y-%m-%d');\n" +
"SET @last24monthsdate = DATE_FORMAT(DATE_SUB(@endDate, INTERVAL 24 MONTH),'%Y-%m-%d');\n" +
"SET @ym=substring(replace(@endDate,'-',''),1,6);\n" +
"\n" +
"    \n" +
"    #_____________________________________________________________________________________________________________________________\n" +
"\n" +
"Select \n" +
"vl_kenyaemr_id\n" +
",AgeYrs\n" +
",Sex\n" +
",cccno\n" +
",HIV_Enrollment_Date\n" +
",ART_Start_Date\n" +
",Last_VL\n" +
",Last_VL_Date\n" +
",Facility_Name\n" +
",MFL_Code\n" +
",Justification\n" +
",PMTCT\n" +
",Yearmonth\n" +
",replace(siku_ya_mwisho,' 00:00:00','') as Last_Visit_Date\n" +
",replace(Next_appointment_date,' 00:00:00','') as Next_appointment_date \n" +
",Indicator\n" +
",regimen_name\n" +
", regimen_line\n" +
" , IFNULL(uzito,'') as 'Weight'\n" +
" ,patient_stable\n" +
" ,Differentiated_care\n" +
" ,poptype as Population_Type\n" +
" \n" +
", key_population_type\n" +
", pregnancy_status \n" +
", expected_delivery_date\n" +
"\n" +
", family_planning_status\n" +
", family_planning_method\n" +
", Screened_for_Cervical_Cancer\n" +
", VisitScheduled\n" +
" ,cacx_screening_6months as cxca_Screened_last6Months\n" +
" ,pregnant_last12months "
        + ",phone_number" +
""
        + " from (" +
"select \n" +
"Concat(siteCode,'_',@ym,'_',m.patient_id) as vl_kenyaemr_id,\n" +
"FLOOR(IFNULL(DATEDIFF(@enddate, STR_TO_DATE(m.DOB, '%Y-%m-%d'))/365,0)) As AgeYrs,\n" +
"Case When m.Gender Is Null Then 'Missing' Else m.Gender End As Sex,\n" +
"case when m.unique_patient_no is null then 'Missing' Else m.unique_patient_no End as cccno ,\n" +
"Case When he.date_confirmed_hiv_positive !='' Then he.date_confirmed_hiv_positive Else 'Missing' End As HIV_Enrollment_Date,\n" +
"Case When (startART.StartARTDate = '1900-01-01 00:00:00.000') Then 'Missing' \n" +
"When startART.StartARTDate   Is NULL Then 'Missing' ELSE startART.StartARTDate   End As ART_Start_Date, \n" +
"Case  When lastVL.LastVL Is Not Null Then lastVL.LastVL Else 'Missing' End As Last_VL,    \n" +
"Case  When (lastVL.LastVLDate = '1900-01-01 00:00:00.000' Or lastVL.LastVLDate Is Null) Then 'Missing' Else lastVL.LastVLDate End As Last_VL_Date, \n" +
"Case  When (lastVL.LastVLDate = '1900-01-01 00:00:00.000' Or lastVL.LastVLDate Is Null) Then 'Missing_VL' Else 'Current_VL' End As Indicator,\n" +
"default_facility_info.FacilityName as Facility_Name,\n" +
"default_facility_info.siteCode as   MFL_Code\n" +
" ,\n" +
" case \n" +
"when order_reason_ ='843' then  'Confirmation of Treatment Failure (Repeat VL)'\n" +
"when order_reason_='1434' then 'Pregnant Mother'\n" +
"when order_reason_='162080' then 'Baseline'\n" +
"when order_reason_='162081' then 'Routine VL' #Follow Up\n" +
"when order_reason_='1259' then 'Single Drug Substitution'\n" +
"when order_reason_='159882' then 'Breast Feeding Mothers'\n" +
"when order_reason_='163523' then 'Clinical Failure'\n" +
"when order_reason_='161236'  then 'Routine VL'\n" +
"else  'Routine VL'\n" +
" end as Justification\n" +
" ,\n" +
"Case\n" +
"when order_reason_='1434' then 'Pregnant'\n" +
"when order_reason_='159882' then 'Breast Feeding'\n" +
"else  'No Data'\n" +
"end as PMTCT\n" +
"\n" +
" ,@ym as Yearmonth\n" +
" , ce.nextapp as next_appointment_date, discontinuation_reason, exitDate\n" +
" , CurReg.rname as regimen_name\n" +
", CurReg.rline as regimen_line\n" +
",uzito\n" +
",siku_ya_mwisho\n" +
" ,ifnull(stability,'') as Patient_stable\n" +
" ,ifnull(dsd,'') as Differentiated_care\n" +
",ifnull(poptype,'General Population') as poptype,\n" +
"ifnull(key_population_type,'') as key_population_type,\n" +
"CASE when m.Gender='F' then ifnull(pregnancy_status,'') else 'NA' end as  pregnancy_status,\n" +
"CASE when m.Gender='F' then ifnull(expected_delivery_date,'') else 'NA' end as  expected_delivery_date,\n" +
"\n" +
"ifnull(family_planning_status,'') as family_planning_status,\n" +
"ifnull(family_planning_method,'') as family_planning_method,\n" +
"ifnull(cacx_screening,'') as Screened_for_Cervical_Cancer, \n" +
"ifnull(visit_scheduled,'') as VisitScheduled,\n" +
"CASE when m.Gender='F' then ifnull(cacx_screening_6months,'No') else 'NA' end as  cacx_screening_6months,\n" +
"CASE when m.Gender='F' then ifnull(prenant_las12months,'No') else 'NA' end as  pregnant_last12months\n" +
",concat(ifnull(phone_number,''),' ') as phone_number\n" +
"from\n" +
" \n" +
" patient_demographics m\n" +
" \n" +
"\n" +
"Left Join ( select patient_id, min(visit_date)MinEnrollment\n" +
", date_first_enrolled_in_care\n" +
", transfer_in_date\n" +
", entry_point\n" +
", date_confirmed_hiv_positive \n" +
"\n" +
"from hiv_enrollment group by patient_id ) he \n" +
"on he.patient_id=m.patient_id\n" +
"\n" +
"/**______Current ON ART______**/\n" +
" Join (\n" +
"/***********************************************************************************************************/\n" +
"select \n" +
"    \n" +
"    fup.visit_date as initial_visit_date,\n" +
"    fup.patient_id as pid,    \n" +
"	greatest(max(fup.visit_date), ifnull(max(d.visit_date),'0000-00-00')) as siku_ya_mwisho,\n" +
"	greatest(mid(max(concat(fup.visit_date,fup.next_appointment_date)),11), ifnull(max(d.visit_date),'0000-00-00')) as nextapp,\n" +
"	mid(max(concat(fup.visit_date,fup.stability)),11) as stability,\n" +
"	d.patient_id as disc_patient,\n" +
"	d.effective_disc_date as effective_disc_date,\n" +
"	max(d.visit_date) as date_discontinued,\n" +
"    mid(max(concat(fup.visit_date,fup.differentiated_care)),11) as dsd\n" +
"    \n" +
"	from hiv_followup fup\n" +
"		join patient_demographics p on p.patient_id=fup.patient_id	\n" +
"	\n" +
"		left outer JOIN\n" +
"		(select patient_id, \n" +
"        coalesce(date(effective_discontinuation_date),visit_date) visit_date,\n" +
"        max(date(effective_discontinuation_date)) as effective_disc_date \n" +
"        from kenyaemr_etl.etl_patient_program_discontinuation\n" +
"		where date(visit_date) <= date(@endDate) and program_name='HIV'\n" +
"		group by patient_id\n" +
"		) d on d.patient_id = fup.patient_id\n" +
"	where fup.visit_date <= date(@endDate)\n" +
"	group by pid\n" +
"	having  (\n" +
"		    ((timestampdiff(DAY,date(nextapp),date(@endDate)) <= 30 or timestampdiff(DAY,date(nextapp),date(curdate())) <= 30) \n" +
"        and (date(d.effective_disc_date) > date(@endDate) or d.effective_disc_date is null))\n" +
"		and (date(siku_ya_mwisho) >= date(date_discontinued) or date(nextapp) >= date(date_discontinued) or disc_patient is null)\n" +
"			)\n" +
"\n" +
"/***********************************************************************************************************/\n" +
"\n" +
"\n" +
") ce on ce.pid=m.patient_id\n" +
"\n" +
"\n" +
"Left Join (Select min(de.date_started) As startARTDate, de.patient_id From drug_event de group by de.patient_id) startART On m.patient_id = startART.patient_id\n" +
"\n" +
"\n" +
"/**______Regimen Line______**/\n" +
"\n" +
"Left Join (Select pel.patient_id,pel.regimen_name as rname, pel.regimen_line as rline From drug_event pel where (discontinued is NULL or discontinued!=1) group by pel.patient_id) CurReg On m.patient_id = CurReg.patient_id\n" +
"\n" +
"/**______Triage______**/\n" +
"\n" +
"\n" +
"Left Join (Select tri.patient_id as tr_pid, weight as uzito From triage tri \n" +
"      join ( select Max(visit_date) as tr_tk , patient_id from triage group by patient_id\n" +
"           ) max_tri on max_tri.patient_id=tri.patient_id\n" +
"        \n" +
" group by tri.patient_id) Curuzito On m.patient_id = Curuzito.tr_pid\n" +
"\n" +
"/**______Viral Load______**/\n" +
"\n" +
"\n" +
" left Join (\n" +
" select  \n" +
"p.patient_id,\n" +
"lastVLDate,\n" +
"max(test_result)  as LastVL\n" +
",oid,\n" +
"ores as order_reason_\n" +
"\n" +
"from laboratory_extract p\n" +
"join \n" +
"(\n" +
" select uuid, order_id as oid\n" +
"from kenyaemr_etl.etl_laboratory_extract  \n" +
") etl \n" +
"on etl.uuid=p.uuid\n" +
"\n" +
"\n" +
"left join\n" +
"\n" +
"(\n" +
"SELECT order_id, order_reason as ores, patient_id FROM openmrs.orders\n" +
") vl_reason on vl_reason.order_id=etl.oid\n" +
"\n" +
"   join \n" +
"  (\n" +
"  SELECT \n" +
"  \n" +
"  max(visit_date) As lastVLDate, \n" +
"  \n" +
"  patient_id\n" +
"   \n" +
"  From laboratory_extract where (lab_test like '%VIRAL%') and (visit_date between @startdate and @enddate )\n" +
"  group by patient_id\n" +
"  \n" +
"  ) h\n" +
"		   on h.patient_id=p.patient_id and p.visit_date=h.lastVLDate where lab_test like '%VIRAL%'\n" +
"          \n" +
"          \n" +
"           group by p.patient_id ) lastVL  \n" +
"           on m.patient_id=lastVL.patient_id\n" +
"           \n" +
"   /*_____________Population Type and other Green Card Items__________*/        \n" +
"Left Join (\n" +
"Select \n" +
"gcf.patient_id as poptype_patient_id,\n" +
"population_type as poptype ,\n" +
"key_population_type,\n" +
"pregnancy_status,\n" +
"expected_delivery_date,\n" +
"\n" +
"family_planning_status,\n" +
"family_planning_method,\n" +
"cacx_screening,\n" +
"visit_scheduled\n" +
"\n" +
"\n" +
"From hiv_followup gcf  \n" +
"join ( select patient_id as poptype_patient_id,  \n" +
"max(visit_date) as pop_type_maxvdate from hiv_followup where date(visit_date)<=date(@EndDate) group by patient_id) max_poptype \n" +
"on max_poptype.poptype_patient_id=poptype_patient_id and pop_type_maxvdate=gcf.visit_date\n" +
"group by gcf.patient_id\n" +
") Curpop\n" +
"\n" +
" \n" +
"On m.patient_id = Curpop.poptype_patient_id\n" +
"\n" +
"\n" +
"/*________________________Check if patient screened for cancer in the last 6 months__________________________________________*/\n" +
"   \n" +
"Left Join \n" +
"(\n" +
"select \n" +
"patient_id as cxca_patient_id, \n" +
"'Yes' as cacx_screening_6months \n" +
"from hiv_followup where \n" +
"( visit_date between @last6monthsdate and @EndDate) and cacx_screening IN ('NEGATIVE','POSITIVE') group by cxca_patient_id   \n" +
"\n" +
") cxca\n" +
"\n" +
" \n" +
"On m.patient_id = cxca.cxca_patient_id\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"/*________________________Check if patient pregnant in the last 12 months__________________________________________*/\n" +
"   \n" +
"Left Join \n" +
"(\n" +
"select \n" +
"patient_id as pmtct_patient_id, \n" +
"'Yes' as prenant_las12months \n" +
"from hiv_followup where \n" +
"( visit_date between @startdate and @EndDate) and pregnancy_status IN ('Yes') group by pmtct_patient_id   \n" +
"\n" +
") pmtct\n" +
"\n" +
" \n" +
"On m.patient_id = pmtct.pmtct_patient_id\n" +
"\n" +
"\n" +
"     \n" +
" /**______discontinuation______**/\n" +
"\n" +
"/**___exclude Patients who have been discontinued in the last quarter _________________**/\n" +
"\n" +
"Left Join ( select distinct p.patient_id, \n" +
"exitDate,\n" +
"discontinuation_reason \n" +
"from patient_program_discontinuation p\n" +
"inner join \n" +
"( SELECT max(visit_date) As exitDate, patient_id From patient_program_discontinuation\n" +
" where program_name ='HIV' group by patient_id ) h\n" +
"		   on h.patient_id=p.patient_id and p.visit_date=h.exitDate \n" +
"           \n" +
"           where DATE_FORMAT(exitDate,'%Y-%m-%d') >=@lastquarterdate\n" +
"           ) ls \n" +
"           on m.patient_id = ls.patient_id\n" +
"  \n" +
"  cross join default_facility_info \n" +
"\n" +
"   order by Last_VL_Date\n" +
"   \n" +
"   \n" +
"   \n" +
"   \n" +
"\n" +
") as dt \n" +
"where 1=1 \n" +
"\n" +
"\n" +
"\n" +
"/** and (Last_VL_Date not in ('Missing')  ) **/\n" +
"\n" +
"group by vl_kenyaemr_id;"
        + "END";
    
        try {
            conn.st.executeUpdate(qry);
            conn.st.executeUpdate(qry1);
            System.out.println("TXCurr Query Created successfully");
        } catch (SQLException ex) 
        {
            Logger.getLogger(update_storedprocedures.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error Creating SP");
        }
    
    return "Complete";
    }
     
}
