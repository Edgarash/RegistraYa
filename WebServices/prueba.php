<?php
    require_once "lib/nusoap.php";
    #$IP = "Servidor:80";
    $IP = "Localhost:8080";
    $cliente = new nusoap_client("http://".$IP."/WebService.php?wsdl");
    $error = $cliente->getError();
    if ($error) {
        echo 'ERROR';
    } else {
        $result = $cliente->call(
            'getReservaciones', array(
                'edgar.31896@gmail.com',
            #'lalalolo1221@gmail.com',
            #'3',
            #'2018-06-04 15:00:00',
            #'2',
            'passWS'=>'RegistraYAMovil'
        ));
        if ($cliente->fault) {
            echo 'FAULT';
        } else {
            var_dump($result);
        }
    }
?>