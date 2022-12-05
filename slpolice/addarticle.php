<?php 
include("db.php");

$date = '0000-00-00';
$title = $_POST['title'];
$body = $_POST['body'];
$cdate = $_POST['cdate'];

$update = "update article set title='$title', body='$body', date='$cdate' where date='$date'";
$sql = "select * from article where date='".$date."'";
$res = mysqli_query($connect,$sql);

while($row = mysqli_fetch_array($res))
{
	if($row['date']=='0000-00-00')
	{
	    if(!mysqli_query($connect,$update))
	    {
	        echo ("notupdate");
	        //printf("Error: %s\n", mysqli_error($connect));
	    }
	    else
	    {
	        echo ("update");
	    }
	}
	else
	{
	    echo ("full");
	}
}
 
mysqli_close($connect);
?>