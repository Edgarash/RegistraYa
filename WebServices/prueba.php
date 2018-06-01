<?php
    require_once "lib/nusoap.php";
    #$IP = "Servidor";
    $IP = "LOCALHOST";
    $cliente = new nusoap_client("http://".$IP.":8080/WebService.php?wsdl");
    $error = $cliente->getError();
    if ($error) {
        echo 'ERROR';
    } else {
        $result = $cliente->call('hacerLogin', array(
            'edgar.31896@gmail.com',
            'MichellT',
            'passWS'=>'RegistraYAMovil'
        ));
        if ($cliente->fault) {
            echo 'FAULT';
        } else {
            var_dump($result);
        }
    }
?>