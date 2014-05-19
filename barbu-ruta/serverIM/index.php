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
$ok = 0;
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
	
		if (isset($_GET["messages"])){
			$ok = 1;
		}
		if (isset($_GET["friends"])){
			$ok = 2;
		} 
		
	
		if ($ok == 0){	
			$idUser = authenticateUser($db, $username, $password);
			if ($idUser == -1){
				print("INCORECT DATA");
				return;
			}
			print("SUCCESS");
		}
		if ($ok == 1){
			print(getMessages($db,$username));
			return;
		}

		if ($ok == 2){
			print(getFriends($db,$username));
			return;
		}	

		break;
	case "sendmessage":
		if (isset($_GET["from"])){
			$from = $_GET["from"];
		}else{
			print(ERROR);
			return;
		}
		if (isset($_GET["to"])){
			$to = $_GET["to"];
		}else{
			print(ERROR);
			return;
		}
		if (isset($_GET["text"])){
			$text = $_GET["text"];
		}else{
			print(ERROR);
			return;
		}

		$query = "insert into messages(from_who,text_,when_,to_) values('" . $from . "', '" . $text . "', NOW(), '" . $to . "')";
		$db->query($query);
		print("SUCCESS");
		return;

		break;
	case "addfriend":
		if (isset($_GET["username"])){
			$username = $_GET["username"];
		}else{
			print(ERROR);
			return;
		}
		
		if (isset($_GET["fromwho"])){
			$fromwho = $_GET["fromwho"];
		}
	
		$email = "";
		$query = "select email from users where username = '" . $fromwho . "'";
		if ($res = $db->query($query)){
			if ($row = $db->fetchObject($res)){
				$email = $row->email; 
			}
		}
		
		$query = "insert into newfriends(username,email,to_) values('" .$fromwho . "', '" .$email. "', '" .$username . "')";
		$db->query($query);


		$id = "";	
		$query = "select * from users where username ='" . $username . "'";
		if ($res = $db->query($query)){
			if ($row = $db->fetchObject($res)){
				$email = $row->email;
				$id = $row->id;
			}
		}

		$out = $username . " " . $email . " " . $id;
		print($out);
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

function getMessages($db, $username){
	$query = "select * from messages where to_='" . $username . "'";
	$ok = 0;
	$ret = "<?xml version=\"1.0\"?>\n<message>\n";
	$count = 0;
	if ($res = $db->query($query)){
		while( $row = $db->fetchObject($res)){
			$ok = 1;
			$ret .= "<mess id=\"" . $count . "\">\n";
			$ret .= "<id>".$row->id."</id>\n";
			$ret .= "<from>".$row->from_who."</from>\n";
			$ret .= "<text>".$row->text_."</text>\n";
			$ret .= "<when>".$row->when_."</when>\n";
			$ret .= "</mess>\n";
			$count = $count + 1;
		}
	}
	$ret .= "</message>";

	$query = "delete from messages where to_='" . $username . "'";
	$db->query($query);
	if ($ok == 0 ){
		return "NU";
	}else{ 
		return $ret;
	}
}
function getFriends($db, $username){
	$query = "select * from newfriends where to_='" . $username . "'";
	$ok = 0;
	$ret = "<?xml version=\"1.0\"?>\n<friends>\n";
	$count = 0;
	if ($res = $db->query($query)){
		while( $row = $db->fetchObject($res)){
			$ok = 1;
			$ret .= "<fr id=\"" . $count . "\">\n";
			$ret .= "<id>".$row->id."</id>\n";
			$ret .= "<username>".$row->username."</username>\n";
			$ret .= "<email>".$row->email."</email>\n";
			//$ret .= "<when>".$row->when_."</when>\n";
			$ret .= "</fr>\n";
			$count = $count + 1;
		}
	}
	$ret .= "</friends>";
	
	$query = "delete from newfriends where to_='" . $username . "'";
	$db->query($query);
	if ($ok == 0 ) {
		return "NU";
	}else{
		return $ret;
	}
}

?>
