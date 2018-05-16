<?php
    require_once "lib/nusoap.php";
    $cliente = new nusoap_client("http://registraya/WebService.php?wsdl");
    $error = $cliente->getError();
    if ($error) {
        echo 'ERROR';
    } else {
        $result = $cliente->call('obtenerRestaurantes', array('PASSWS'=>'RegistraYAMovil'));
        if ($cliente->fault) {
            echo 'FAULT';
        } else {
            var_dump($result);
        }
    }
?>