<?php
    class Conexion {
        private $Usuario = "AdministradorRY";

        private $Servidor = "Servidor";
        #private $Servidor = "Servidor";
        
        private $Contraseña = "Admin";
        private $BD = "RegistraYA";

        public function Conectar() {
            try {
                if (!($link = new PDO("mysql:host=$this->Servidor;dbname=$this->BD;", $this->Usuario, $this->Contraseña))) {
                    echo "Error al intentar conectar con la base de datos";
                    exit();
                }
             } catch (PDOException $e) {
                echo "ERROR: ".$e->getMessage();
            }
            $link->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            return $link;
        }
    }

    function ConectarBD() {
        $temp = new Conexion();
        return $temp->Conectar();
    }
?>