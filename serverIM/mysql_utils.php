<?php

class MySql{
	private $dbLink;
	private $dbHost;
	private $dbUsername;
	private $dbName;
	private $dbPassword;


	//constructor
	function __construct($dbHost,$dbUsername,$dbPassword,$dbName){
		$this->dbHost = $dbHost;
		$this->dbUsername = $dbUsername;
		$this->dbPassword = $dbPassword;
		$this->dbName = $dbName;
	}

	function __destruct(){
		$this->close();
	}


	//connect to the database
	function connect() { 
		$this->dbLink = mysql_connect($this->dbHost, $this->dbUsername, $this->dbPassword, $this->dbName);
		if (!$this->dbLink){
			//echo "false";
			return false;
		}else if (!mysql_select_db($this->dbName, $this->dbLink)){
			//echo "true";
			return false;
		}else{
			return true;
		}	
	}

	function close(){
		@mysql_close($this->dbLink);
	}


	//execute sql queries
	function query($sql){
		if (!$this->dbLink){
			$this->connect();
		}

		if ( ! $res = mysql_query($sql, $this->dbLink)){
			return false;
		}
		return $res;

		
	}

	//return values in an array
	function fetchObject($res){
		$object = mysql_fetch_object($res);
		if ( !$object){
			return false;
		}else{
			return $object;	
		}
	}

	//return number of rows of a result
	function getNumRows($res){
		return mysql_num_rows($res);
	}

}


?>
