E-commerce Platform Backend 🛒  

This repository hosts the backend for a robust e-commerce platform, built with Spring Boot. It features comprehensive product and user management, shopping cart and order processing, a review system, and integrated payment processing with Razorpay.  

Features   

1.Product Management: Add, view, update, and delete products.    
2.User Management: Secure user registration and authentication.    
3.Shopping Cart: Add products to cart, update quantities, remove items, clear cart.    
4.Order Processing: Create orders from the shopping cart.  
5.User Reviews & Ratings: Submit and view product reviews.  
6.Razorpay Payment Integration:  
Initiate payment orders.    
Verify payment status.  
Handle pending, paid, and failed order statuses.  
7.Comprehensive Logging: Configurable logging for user actions, admin operations, and system events.    
8.Global Error Handling: Consistent API error responses for various exceptions.    
9.JUnit Testing: Unit tests for core service logic.    

Project Setup ⚙️    

Prerequisites:    

Before you begin, ensure you have the following installed:      
=>Java Development Kit (JDK): Version 17 or higher.    
=>Apache Maven: Version 3.8.x or higher.    
=>MySQL Database: Version 8.x or higher.    
=>Postman (or any API testing tool): To test the API endpoints.    

1. Clone the Repository:  

git clone <your-repository-url>      
cd e-commerce-platform    
    
2.Database Configuration    
Create a MySQL database named E_Commerce.    
  
Update the src/main/resources/application.properties file with your MySQL database credentials and Razorpay API keys.    
razorpay.key.id=rzp_test_YOUR_KEY_ID      
razorpay.key.secret=YOUR_KEY_SECRET    
  
(Replace rzp_test_YOUR_KEY_ID and YOUR_KEY_SECRET with your actual Razorpay Test Key ID and Secret from your Razorpay Dashboard.)    
    
3.Build and Run the project    
The application will start on http://localhost:8080    
  
API ENDPOINTS:    
All endpoints are relative to http://localhost:8080    
  
1. User Management    
     
Register a New User:      
  
Endpoint: /register    
Method: POST    
Description: Creates a new user account and an associated shopping cart.    
Request Body (JSON):  
{  
    "username": "testuser",  
    "password": "securepassword",  
    "email": "user@example.com",  
    "fullName": "Test User",  
    "phone": "1234567890",  
    "address": "123 Main St"  
}  
<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/69e5b349-3e7d-4269-b9d1-029467b7e4a5" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/3ea4c5e8-ea2a-4163-9ceb-2827673d4ee7" />


  
Login Page Information:  
  
Endpoint: /perform_login    
Method: POST  
Description: Actual login typically happens via /perform_login handled by Spring Security.  
Request Body (JSON):  
{  
    "username": "testuser",  
    "password": "securepassword",  
}  

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/952b3100-b994-4a19-9b46-6af39b4e762a" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/44b8f6c5-91ec-431e-830c-96e5c10a7fd7" />

  
2. Product Management:  
Add a Product:(Only accessible to ADMIN)  
Endpoint: admin/products/add  
Method: POST  
Description: Adds a new product to the inventory.  
Request Body (JSON):  
{  
    "name": "Gaming Laptop",    
    "description": "High-performance laptop for serious gamers.",  
    "price": 1500.00,  
    "stockQuantity": 5,  
    "category": "Electronics"  
}
<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/05c396d1-dcf6-41ec-b770-30e01c3b91e7" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/0bcfdbcf-1d6b-405f-9caf-bd28315af211" />

  
Update a Product:(Only accessible to ADMIN)  
Endpoint: admin/products/{id}  
Method: PUT  
Description: Updates a existing product in the inventory.  
Request Body (JSON):  
{  
    "name": "Gaming Laptop Updated",  
    "description": "High-performance laptop for serious gamers.",  
    "price": 2500.00,  
    "stockQuantity": 15,  
    "category": "Electronics"  
}  
<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/05c396d1-dcf6-41ec-b770-30e01c3b91e7" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/e9f02251-997b-481c-a18e-c61e50b5bb3a" />

  
Delete a Product:(Only accessible to ADMIN)  
Endpoint: admin/products/{id}  
Method: DELETE  
Description: Deletes a product from the inventory.  
<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/05c396d1-dcf6-41ec-b770-30e01c3b91e7" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/a7fbb052-28b6-44ba-bf4c-2949513307a5" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/6768a2a9-efbb-4f34-9306-24551839ba97" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/b26964a6-d8cc-409c-8aef-ff382601c173" />

  
Retrieve all Products:(Accessible to PUBLIC)  
Endpoint: public/products  
Method: GET  
Description: Retrieves all products from the inventory.  

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/952b3100-b994-4a19-9b46-6af39b4e762a" />

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/a7fbb052-28b6-44ba-bf4c-2949513307a5" />

  
3. Cart Management:  
  
Add Item to Cart:  
Endpoint: /cart/addItem  
Method: POST  
Description: Adds a specified quantity of a product to a user's cart  
Authentication: Assumes user is authenticated (username pulled from Spring Security context).  
  
Query Parameters:  
product_name: (e.g.Gaming Laptop Updated)  
quantity: (e.g., 2)  

<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/bb4a5797-15a1-4d59-901e-dee9e82e44e3" />




Get Cart Details:  
Endpoint: /cart/getItems  
Method: GET  
Description: Retrieves the current contents of a user's shopping cart.  

Remove Item from Cart:  
Endpoint: /cart/removeItem  
Method: DELETE  
Description: Removes a product entirely from a user's cart.  
Response Body:JSON  
{  
"name": "Gaming Laptop Updated"  
}  

Clear Cart:  
Endpoint: /cart/clear  
Method: DELETE  
Description: Clears all items from a user's cart.  

4. Order Processing:  
     
Create an Order:  
Endpoint: /orders/create  
Method: POST  
Description: Creates a PurchaseOrder in your system directly from the user's cart. This method is suitable for scenarios like Cash on Delivery (CoD) where payment is assumed, or for internal order tracking before external payment. The cart is cleared and stock is reduced.  
Authentication: Assumes user is authenticated (username pulled from Spring Security context).  
Success Response: 201 Created with the internal PurchaseOrder details.  
{  
    "id": 5,  
    "user": {  
        "user_id": 2,  
        "fullName": "anjali",  
        "username": "anjali",  
        "email": "jane.doe@example.com",  
        "phone": "9876543210",  
        "address": "123 Commerce Street, Anytown",  
        "role": "user"    
    },  
    "items": [
    {
            "id": 9,
            "product": {
                "product_id": 1,
                "name": "Laptop",
                "description": "Powerful laptop...",
                "price": 1200.0,
                "category": "Electronics"
            },
            "quantity": 1,
            "price": 1200.0
        }
    ],  
    "orderDate": "2025-08-17T09:50:36.368+00:00",  
    "status": "PAID", # Or PENDING, depending on your latest OrderService  
    "totalAmount": 1251.0,  
    "razorpayOrderId": order_punsudg7283,  
    "razorpayPaymentId": null  
}  
  
Get All Orders for a User  
Endpoint: /orders/getOrders  
Method: GET  
Description: Retrieves all purchase orders for the authenticated user.  

Update Order Status  
Endpoint: /orders/updateStatus  
Method: PUT  
Description: Updates the status of a specific order (e.g., to "SHIPPED", "DELIVERED"). This is an ADMIN action.  

5.Razorpay Payment Processing   
This section outlines the two-step process for integrating with Razorpay through your backend.  

Step 1: Create a Razorpay Order (Backend initiates payment)  
Endpoint: /payment/create  
Method: POST  
Description: Your backend requests Razorpay to create a payment order. This generates a razorpay_order_id which the frontend typically uses to open Razorpay's checkout popup.    

Query Parameters:  
amount: The total amount for the payment in Rupees (e.g., 1500).  
productName: A brief description for the Razorpay order (e.g., E-commerce Purchase)  

Success Response: 200 OK with a JSON string representation of the Razorpay Order.     
{  
  "id": "order_XXXXXXXXXXXXXX",  
  "entity": "order",  
  "amount": 150000, // Amount in paise  
  "currency": "INR",  
  "receipt": "txn_YYYYYYYYYYYYY",    
  "status": "created"  
}  

Step 2: Verify Razorpay Payment (Backend receives callback)  
Endpoint: /payment/verify  
Method: POST  
Description: This endpoint simulates the callback your backend would receive from Razorpay (or your frontend after the user completes payment on Razorpay's checkout popup). Your backend uses the received razorpay_order_id, razorpay_payment_id, and razorpay_signature to cryptographically verify the payment's authenticity with Razorpay's SDK.  
Request Body (JSON):  
IMPORTANT: When testing without a real frontend, use the razorpay_order_id you got from Step 1. The razorpay_payment_id and razorpay_signature will be dummy values, so the verification will fail (which is expected for this testing method), but it confirms your endpoint is working.  

{  
    "razorpayOrderId": "order_XXXXXXXXXXXXXX",      # Use the ID from Step 1  
    "razorpayPaymentId": "pay_SIMULATEDID12345",   # Dummy ID for testing without frontend  
    "razorpaySignature": "sig_SIMULATEDSIGNATUREABCD" # Dummy signature for testing  
}  
  
6. User Reviews and Ratings  
Add a Product Review    
Endpoint: /reviews/addReview  
Method: POST  
Description: Allows a user to submit a review and rating for a product.  
Authentication: Assumes user is authenticated (username pulled from Spring Security context).  

Query Parameters:  
product_name: (e.g., Gaming Laptop)  
rating: (e.g., 5 - integer from 1 to 5)  
comment: (e.g., Excellent performance!)  

Get Reviews for a Product  
Endpoint: /reviews/product/{product_name}  
Method: GET  
Description: Retrieves all reviews for a specific product.  
Path Variable: product_name (e.g., /reviews/product/Gaming Laptop)  

Logging & Error Handling  
  
The system implements robust logging and centralized error handling for better monitoring and maintainability.  
Logging: Configured via application.properties to produce detailed logs.  
Log files are stored in the logs/ directory.  
Logs are rotated daily, creating new files with the date (ecommerce.log.YYYY-MM-DD.i.log).  
Logs capture user transactions, admin actions, and system errors at INFO, WARN, ERROR, and DEBUG levels.  

Error Handling:  
  
A GlobalExceptionHandler (using @RestControllerAdvice) provides consistent JSON error responses for API consumers.  
Specific exceptions (like UserNotFoundException, ProductNotFoundException) return 404 Not Found.  
Validation errors (MethodArgumentNotValidException) return 400 Bad Request with details.  
Generic business logic RuntimeExceptions return 400 Bad Request.  
All other unhandled exceptions are caught, logged (with full stack trace), and returned as 500 Internal Server Error with a generic message to prevent exposing internal details.  

Testing   
The project includes unit tests to ensure the reliability and correctness of the core business logic.  
Unit Tests: Located in src/test/java/com/ecommerce/platform/.  
Utilize JUnit 5 for the testing framework.  
Employ Mockito for mocking dependencies, allowing isolated testing of service layer logic.  
