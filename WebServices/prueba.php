<?php
    require_once "lib/nusoap.php";
    $cliente = new nusoap_client("http://192.168.1.71:8080/webservices/serverwebCues.php?wsdl");
    $error = $cliente->getError();
    if ($error) {
        echo 'ERROR';
    } else {
        $result = $cliente->call('login', array(
            'correo' => 'cg_0196@hotmail.com',
            'contrasena' => '12345',
            'passWS'=>'cuestionario2018'
        ));
        if ($cliente->fault) {
            echo 'FAULT';
        } else {
            var_dump($result);
        }
    }
?>