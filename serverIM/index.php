<?php

//these 3 rows used for debug - shows errors gave by php
error_reporting(E_ALL);
ini_set('display_errors', TRUE);
ini_set('display_startup_errors', TRUE);

require_once("mysql_utils.php");

$dbHost = "localhost";
$dbUsername = "pdsd";
$dbPassword = "pdsd1234";
$dbName = "PDSD_DB";

define("ERROR", 1);
define("WRONG_USER",2);
define("SUCCESFULL",3);

$db = new MySql($dbHost, $dbUsername, $dbPassword, $dbName);

//print("Inainte\n");
$connection = $db->connect();
if (!$connection){
	print("COULD NOT CONNECT");
}

//print( "dupa\n");

if (isset($_GET["type"])){
	$type = $_GET["type"];
} else{
	print(ERROR);
	return;
}
switch($type){
	case "signup":
		if (isset($_GET["username"])){
			$username = $_GET["username"];
		}else{
			print(ERROR);
			return;
		}
		if (isset($_GET["email"])){
			$email = $_GET["email"];
		}else{
			print(ERROR);
			return;
		}
		if (isset($_GET["password"])){
			$password = $_GET["password"];
		}else{
			print(ERROR);
			return;
		}
		
		$query = "select id from users where username = '" . $username . "'limit 1";
		if ($res = $db->query($query)){
			if ($db->getNumRows($res) == 0){
				$query = "insert into users(username,password,email,date) values('" . $username . "', '" . $password . "', '" . $email . "', NOW())" ;
				if ($db->query($query)){
					print("SUCCESS");
				}else{
					print("COULD NOT ADD USER");
				}
			}else{
				print("USER EXISTENT");
			}
		}	


		break;
	case "signin":
		if (isset($_GET["username"])){
			$username = $_GET["username"];
		}else{
			print(ERROR);
			return;
		}
		if (isset($_GET["password"])){
			$password = $_GET["password"];
		}else{
			print(ERROR);
			return;
		}
		
//		print("aici");
		$idUser = authenticateUser($db, $username, $password);
//		print($idUser);
		if ($idUser == -1){
			print("INCORECT DATA");
			return;
		}


		break;
	case "message":

		break;
	case "addFriend":

		break;
}

function authenticateUser($db, $username, $password){
	$query = "select * from users where username='" . $username . "' and password = '" . $password . "' limit 1";
	$retId = -1;
	if ($res = $db->query($query)){
		if ($row = $db->fetchObject($res)){
			$retId = $row->id;
			$query = "update users set auth_time = NOW() where id = '" . $retId . "' limit 1";
			$db->query($query);
		}
	}
	return $retId;
}


?>
