<?php 
include("db.php");

$cat = $_POST['cat'];

$sql = "select * from laws where category='$cat'";
$res = mysqli_query($connect,$sql);
$result = array();

while($row = mysqli_fetch_array($res))
{
    if($row['type']=="Amendment")
    {
        array_push($result,array('name'=>$row[2],'language'=>$row[5],'url'=>$row[3]));
    }
}
echo json_encode(array('result'=>$result));
 
mysqli_close($connect);
?>