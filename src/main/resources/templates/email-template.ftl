<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Password Reset Request</title>
    <style>
        body {
            font-family: Arial, Helvetica, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .email-container {
            width: 100%;
            background-color: #f4f4f4;
            padding: 20px 0;
            display: flex;
            justify-content: center;

        }
        .email-content {
            width: 600px;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin: auto;
        }
        .email-header {
            background-color: #0073e6;
            color: #ffffff;
            padding: 20px;
            text-align: center;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
        }
        .email-header h1 {
            margin: 0;
            font-size: 32px;
        }
        .email-body {
            padding: 20px;
            color: #333333;
            line-height: 1.6;
        }
        .email-body h2 {
            color: #555555;
            font-size: 24px;
        }
        .email-body p {
            font-size: 16px;
            margin: 0;
        }
        .verification-code {
            font-size: 24px;
            font-weight: bold;
            color: #0073e6;
            margin-top: 20px;
            text-align: center;
        }
        .email-footer {
            margin-top: 20px;
            text-align: center;
            font-size: 14px;
            color: #888888;
        }
    </style>
</head>
<body>
    <div class="email-container">
        <div class="email-content">
            <div class="email-header">
                <h1>Password Reset Code</h1>
            </div>
            <div class="email-body">
                <h2>Hello ${name},</h2>
                <p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu của bạn. Dưới đây là mã xác minh của bạn:</p>
                <div class="verification-code">${otp}</div>
                <p>Vui lòng nhập mã này vào ứng dụng để tiến hành đặt lại mật khẩu của bạn.</p>
                <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
                <p>Thank you,<br>The Support Team</p>
            </div>
            <div class="email-footer">
                <p>&copy; ${name}. All rights reserved.</p>
                <p>${location}</p>
            </div>
        </div>
    </div>
</body>
</html>