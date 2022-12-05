<?php 
include("db.php");

$sql = "select * from digs";
$res = mysqli_query($connect,$sql);
$result = array();

while($row = mysqli_fetch_array($res))
{
    if($row['type']=="SDIG")
    {
        array_push($result,array('name'=>$row[1],'aria'=>$row[2],'mobile'=>$row[3],
	'office'=>$row[4],'fax'=>$row[5]));
    }
}
echo json_encode(array('result'=>$result));
 
mysqli_close($connect);
?>