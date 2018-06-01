<?php
    #Añadiendo Libreria
    require_once "lib/nusoap.php";
    require_once "Conexion.php";
    #SOAP SERVER
    $Server = new soap_server();
    #Encoding
    $Server->soap_defencoding = 'UTF-8';
    $Server->decode_utf8 = false;
    $Server->encode_utf8 = true;
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
        } else {    //PASSWS INCORRECTO
            getError(100, $PASSWS);
        }
    }

    $Server->Register(
        'registrarUsuario',                 #Nombre del Método
        array(
            'Correo' => 'xsd:string',
            'Password' => 'xsd:string',
            'Nombre' => 'xsd:string',
            'Apellidos' => 'xsd:string',
            'PASSWS' => 'xsd:string'
        ),                                  #Parámetros de Entrada  
        array(
            'return' => 'xsd:string'        #Parámetros de Salida
        ),
        'RegistraYA',                       #Namespace
        'RegistraYA/registrarUsuario',      #SOAPaction
        'rpc',                              #Style
        'encoded',                          #Use
        'Registra un Usuario en la Base
        de Datos.'                          #Documentacion
    );

    function registrarUsuario($Correo, $Password, $Nombre, $Apellidos, $PASSWS) {
        if ($PASSWS == 'RegistraYAMovil') {
            if (validarNombreApellidos($Nombre)) {
                if (validarNombreApellidos($Apellidos)) {
                    if (validarPassword($Password)) {
                        if (validarCorreo($Correo)) {
                            if (!existeUsuario($Correo)) {
                                $Link = ConectarBD();
                                $SQL = "INSERT INTO Usuarios VALUES (:Email, :Contrasena, :Nombre, :Apellidos);";
                                $STMT = $Link->prepare($SQL);
                                $STMT->bindParam(':Email', $Correo);
                                $STMT->bindParam(':Contrasena', $Password);
                                $STMT->bindParam(':Nombre', $Nombre);
                                $STMT->bindParam(':Apellidos', $Apellidos);
                                $STMT->execute();
                                if ($STMT->rowCount() > 0) {
                                    return 'USUARIO INGRESADO CON EXITO.';
                                } else {    //CORREO NO SE PUDO REGISTRAR
                                    return getError(4, $Correo);
                                }
                            } else {    //CORREO YA REGISTADO
                                return getError(1, $Correo);
                            }
                        } else {    //CORREO NO VALIDO
                            return getError(3, $Correo);
                        }
                    } else {    //CONTRASEÑA NO VALIDA
                        if (strlen($PASSWS) < 8) 
                            return getError(12, $Password);
                        else
                            return getError(10, $Password);
                    }
                } else {    //APELLIDOS NO VALIDOS
                    return getError(30, $Apellidos);
                }
            } else {    //NOMBER NO VALIDO
                return getError(20, $Nombre);
            }
        } else {    //PASSWS NO VALIDO
            return getError(100, $PASSWS);
        }
    }

    $Server->Register(
        'hacerLogin',                       #Nombre del Método
        array(
            'Correo' => 'xsd:string',
            'Password' => 'xsd:string',
            'PASSWS' => 'xsd:string'
        ),                                  #Parámetros de Entrada  
        array(
            'return' => 'xsd:string'        #Parámetros de Salida
        ),
        'RegistraYA',                       #Namespace
        'RegistraYA/hacerLogin',            #SOAPaction
        'rpc',                              #Style
        'encoded',                          #Use
        'Registra un Usuario en la Base
        de Datos.'                          #Documentacion
    );

    function hacerLogin($Correo, $Password, $PASSWS) {
        if ($PASSWS == 'RegistraYAMovil') {
            if (validarCorreo($Correo)) {
                if ($Usuario = existeUsuario($Correo)) {
                    if ($Usuario->Password == $Password) {
                        return json_encode($Usuario);
                    } else {    //PASSWORD NO COINCIDE
                        return getError(11, $Password);
                    }
                } else {    //CORREO NO EN BASE DE DATOS
                    return getError(2, $Correo);
                }
            } else {    //CORREO NO VALIDO
                return getError(3, $Correo);
            }
        } else {    //PASSWS NO VALIDO
            return getError(100, $PASSWS);
        }
    }

    function getError($noError, $Cadena) {
        switch ($noError) {
            case 1:
                return 'ERROR 001: EL CORREO "'.$Cadena.'" YA REGISTADO.';
                break;
            case 2:
                return 'ERROR 002: EL CORREO "'.$Cadena.'" NO ESTÁ REGISTADO.';
                break;
            case 3:
                return 'ERROR 003: EL CORREO "'.$Cadena.'" NO ES VÁLIDO.';
                break;
            case 4:
                return 'ERROR 004: EL CORREO "'.$Cadena.'" NO SE PUDO REGISTRAR.';
                break;
            case 10:
                return 'ERROR 010: LA CONTRASEÑA "'.$Cadena.'" NO ES VÁLIDA, DEBE TENER COMO MÍNIMO 8 CARACTERES, UNA LETRA Y UN NÚMERO..';
                break;
            case 11:
                return 'ERROR 011: LA CONTRASEÑA "'.$Cadena.'" NO ES CORRECTA.';
                break;
            case 12:
                return 'ERROR 012: LA CONTRASEÑA DEBE TENER MÍNIMO 8 CARACTERES, UNA LETRA Y UN NÚMERO.';
                break;
            case 20:
                return 'ERROR 020: EL NOMBRE "'.$Cadena.'" NO ES VÁLIDO.';
                break;
            case 30:
                return 'ERROR 030: APELLIDO "'.$Cadena.'" NO VÁLIDO.';
                break;
            case 100:
                return 'ERROR 100: EL PASSWORD "'.$Cadena.'" NO ES CORRECTO.';
                break;
        }
    }

    function existeUsuario($Correo) {
        $Link = ConectarBD();
        $SQL = "SELECT * FROM Usuarios WHERE Email = :Correo;";
        $STMT = $Link->prepare($SQL);
        $STMT->bindParam(':Correo', $Correo);
        $STMT->execute();
        if ($fila = $STMT->fetch()) {
            $temp = new stdClass();
            $temp->Correo = $fila['Email'];
            $temp->Password = $fila['Contrasena'];
            $temp->Nombre = $fila['Nombre'];
            $temp->Apellidos = $fila['Apellidos'];
            return $temp;
        } else {
            return false;
        }
    }

    function validarCorreo($Correo) {
        if (filter_var($Correo, FILTER_VALIDATE_EMAIL)) {
            return true;
        } else {
            return false;
        }
    }

    function validarPassword($Password) {
        if (preg_match("/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!$%@#£€*?&]{8,}$/", $Password)) {
            return true;
        } else {
            return false;
        }
    }

    function validarNombreApellidos($Cadena) {
        if (preg_match("/^[A-Za-záéíóúÁÉÍÓÚ]+\s*()[A-Za-záéíóúÁÉÍÓÚ]*$/", $Cadena)) {
            return true;
        } else {
            return false;
        }
    }

    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA = file_get_contents( 'php://input' );
    $Server->service($HTTP_RAW_POST_DATA);
?>