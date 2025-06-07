package com.oo2.grupo9.helpers;

public class ViewRouteHelper {
    //Usuarios
    public final static String INDEX = "home/index";
    public final static String USUARIO_INSCRIBIRSE = "usuarios/inscribirse";
    public final static String USUARIO_LOGIN = "usuarios/Login";
    public final static String USUARIO_LISTA = "usuarios/ListaUsuario";
    public final static String USUARIO_ADMIN_CREAR = "admin/agregar-usuario";
    public final static String USUARIO_ADMIN_MODIFICAR = "admin/modificar-usuario";
    public final static String ADMIN_USER_SEARCH_RESULTS = "admin/resultados-busqueda-usuarios";
    
    //Tickets
    public final static String TICKET_CREAR = "tickets/CrearTicket";
    public final static String LISTA_TICKETS = "tickets/ListaTickets";
    public final static String VER_TICKET = "tickets/VerTicket";
    public final static String TICKETS_SEARCH_RESULTS = "tickets/ResultadosBusquedaTickets";
    public final static String ADMIN_TICKET_PANEL = "admin/panel-tickets";
    public final static String MIS_TICKETS = "tickets/mis-tickets";
    public final static String BUSCAR_TICKETS = "tickets/ResultadosBusquedaTicketsCliente";
    //Intervencion
    public final static String ADMIN_INTERVENTION_PANEL = "admin/panel-intervenciones";
    public final static String ADMIN_INTERVENTION_SEARCH_RESULTS = "admin/resultados-intenvenciones";

    //Redirects
    public final static String ROUTE_INDEX = "/";
}
