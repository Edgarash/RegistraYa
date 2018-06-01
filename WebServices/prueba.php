<?php
    require_once "lib/nusoap.php";
    $IP = "Servidor";
    $cliente = new nusoap_client("http://".$IP.":8080/WebService.php?wsdl");
    $error = $cliente->getError();
    if ($error) {
        echo 'ERROR';
    } else {
        $result = $cliente->call('obtenerRestaurantes', array(
            'passWS'=>'RegistraYAMovil'
        ));
        if ($cliente->fault) {
            echo 'FAULT';
        } else {
            var_dump($result);
        }
    }
?>