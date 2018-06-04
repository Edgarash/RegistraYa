<?php
    require_once "lib/nusoap.php";
    #$IP = "Servidor";
    $IP = "Localhost";
    $cliente = new nusoap_client("http://".$IP.":8080/WebService.php?wsdl");
    $error = $cliente->getError();
    if ($error) {
        echo 'ERROR';
    } else {
        $result = $cliente->call('hacerReservacion', array(
            'lalalolo1221@gmail.com',
            '3',
            '2018-06-04 15:00:00',
            '2',
            'passWS'=>'RegistraYAMovil'
        ));
        if ($cliente->fault) {
            echo 'FAULT';
        } else {
            var_dump($result);
        }
    }
?>