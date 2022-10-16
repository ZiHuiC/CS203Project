# CS203Project
# Lend a Hand
Started on 13/09/2022
## To do:
1. Login
2. Sign up sends email for confirmation
3. Make sure only admins get access to all the listings/users/applications
4. Check Application mapping 
5. User controller


## 02/10/2022 - Add Listings!
Guys, FINALLY! We have the feature of adding LISTINGS!
Be excites!!!!! @JsonIgnore was the answer lmaooo ~ Adrian

## 05/10/2022 - Cyclical dependency
DO NOT RETURN THE OBJECT ITSELF. WHEN SPRINGBOOT TRIES TO CREATE THE JSON, IT WILL LEAD TO A STACK OVERFLOW.
Finally, all of the mappings work. 

## Note
should include get username because getting by id makes no sense when u login by username
also need to have UserAlreadyExistException in addUser

### Get all listings
/listingpage

### Get a listing
/listingpage/{id}

### Create a Listing 
/listingpage/createlisting?userId={id}

{
"name" : X,
"des" : X,
"noOfParticipants" : X
}

### Create a User
/signup

AUTH should not be decided here

{
 "username" : "someone@email.com";
"password" : "qwert12345";
"firstname" : "Someone";
"lastname" : "Tan";
"contactNo" : "62353535";
"authorities" : "AUTH_USER";
}

### View all users
/profiles


