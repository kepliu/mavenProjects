<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html" >
      
<!--  
URL for this page:  http://localhost:8080/jpatestWeb/faces/jpatest.jsp

<html>
   <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
   <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
-->
      
      
   <f:view>
      <head>                  
         <title><h:outputText value="HI there"/></title>
      </head>
      <body>
         <h:form>
            <h3>
               <h:outputText value="HI there"/>
            </h3>
                   
            <p>
               <h:outputText value="#{testBean.msg}"/> 
               <h:inputText value="#{testBean.text}"/>
            </p>
            <p>
               <h:commandButton value="submit" action="#{testBean.doSubmit}"/>
            </p>
           
         </h:form>
      </body>
   </f:view>
</html>
