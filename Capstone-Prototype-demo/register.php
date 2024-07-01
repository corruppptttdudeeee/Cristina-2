<?php
// Configuration
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "login_system";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: ". $conn->connect_error);
}

// Check if form is submitted
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST["username"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $confirm_password = $_POST["confirm_password"];

    // Check if passwords match
    if ($password != $confirm_password) {
        $error = "Passwords do not match";
    } else {
        // Hash password
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);

        // Insert data into database
        $query = "INSERT INTO login_systems (username, email, password) VALUES ('$username', '$email', '$hashed_password')";
        $result = $conn->query($query);

        if ($result) {
            // Sign up successful, redirect to login page
            header('Location: login.html');
            exit;
        } else {
            // Sign up failed, display error message
            $error = "Failed to create account";
        }
    }
}

// Close connection
$conn->close();
?>

<!-- Display error message if any -->
<?php if (isset($error)) { ?>
    <p style="color: red;"><?php echo $error; ?></p>
<?php } ?>

<!DOCTYPE html>
<html>
<head>
	<title>Sign Up</title>
</head>
<body>
	<h1>Sign Up</h1>
	<form action="register.php" method="post">
		<label for="username">Username:</label>
		<input type="text" id="username" name="username"><br><br>
		<label for="email">Email:</label>
		<input type="email" id="email" name="email"><br><br>
		<label for="password">Password:</label>
		<input type="password" id="password" name="password"><br><br>
		<label for="confirm_password">Confirm Password:</label>
		<input type="password" id="confirm_password" name="confirm_password"><br><br>
		<input type="submit" value="Sign Up">
	</form>
</body>
</html>