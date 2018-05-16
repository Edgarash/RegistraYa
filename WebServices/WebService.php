<?php
    #Añadiendo Libreria
    require_once "lib/nusoap.php";
    require_once "Conexion.php";
    #SOAP SERVER
    $Server = new soap_server();
        #Encoding
        $Server->soap_defencoding = 'UTF-8';
        $Server->configureWSDL('ServicioWeb', 'RegistraYA');

    #Registrando los Servicios
    #Registrando ObtenerRestaurantes
    $Server->Register(
        'obtenerRestaurantes',              #Nombre del Método
        array(
            'PASSWS' => 'xsd:string'
        ),                                  #Parámetros de Entrada  
        array(
            'return' => 'xsd:string'        #Parámetros de Salida
        ),
        'RegistraYA',                       #Namespace
        'RegistraYA/obtenerRestaurantes',   #SOAPaction
        'rpc',                              #Style
        'encoded',                          #Use
        'Obtiene un array de todos los 
        restaurantes de la base de datos'   #Documentacion
    );

    #Funcion ObtenerRestaurantes -> Obtiene todos los restaurantes de la base de datos
    function obtenerRestaurantes($passws) {
        if ($passws == 'RegistraYAMovil') {
            $Link = ConectarBD();
            #Consulta
            $SQL = "SELECT * FROM getRestaurantes;";
            $Resultado = $Link->query($SQL);
            $Object = array();
            foreach ($Resultado as $fila) {
                $temp = new stdClass();
                $temp->ID = $fila['ID'];
                $temp->Nombre = $fila['Nombre'];
                $temp->Colonia = $fila['Colonia'];
                $temp->Latitud = $fila['Latitud'];
                $temp->Longitud = $fila['Longitud'];
                $temp->HorarioApertura = $fila['HorarioApertura'];
                $temp->HorarioCierre = $fila['HorarioCierre'];
                $Object[] = $temp;
            }
            return json_encode($Object);
        } else {
            return 'CONTRASEÑA INCORRECTA';
        }
    }
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA = file_get_contents( 'php://input' );
    $Server->service($HTTP_RAW_POST_DATA);
?>