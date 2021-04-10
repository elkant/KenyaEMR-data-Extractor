<%-- 
    Document   : recreatetables
    Created on : Mar 25, 2021, 10:06:10 AM
    Author     : EKaunda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

 <link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
   <link href="assets/css/style.css" rel="stylesheet" />
   <link href="assets/bootstrap/css/bootstrap.css" rel="stylesheet" />
   <link rel="shortcut icon" href="images/afq.png"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Recreate Data Tables</title>
    </head>
    <body>
       <div class="container-fluid">
        <form class="form-horizontal" name="reacreatetables" action="recreateTables">
            <div class="form-actions">
            <input  class="btn btn-success" type="submit" value="Recreate"    />
            
            <div id="logs" style="color:orange;"></div>
            
        </div>
        </form>
        
    </body>
    
    
    <script src="js/jquery.min.js"></script>  
    <script src="assets/bootstrap/js/bootstrap.js"></script>  
    
    
    <script>
      
      function refreshLogs(){
       $.ajax({
url:'showquerystatus',
type:'get',
dataType:'json',
success:function (data){
     $('#logs').html(data.qry);   
}

});
      }
      
       $("form").submit(function(){
      
       setInterval(function() {
      refreshLogs();
      }, 100);
      
    });
   

    
        
    </script>
    
    </div>
</html>
