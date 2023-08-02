# todo-list_app
Firebase, NavigationView, RecyclerView, ViewBinding, Fragments, Dialog, Custom, Figma

As soon as the user launches the application, an introductory fragment appears. "Introductory fragment": text, picture and button.
Then, by clicking on the "Start" button, the transition to the registration fragment takes place.
"Registration": fields for entering a name, email, creating and repeating a password; it is possible to go to the login fragment.

![image](https://github.com/xemura/todo-list_app/assets/92382028/2aaee1f4-f861-4907-9362-26537e88266b)

"Login fragment": fields for entering mail, password; it is possible to switch to a fragment with registration and password change.
After clicking on the register and log in buttons, the user is transferred to the main fragment, where all his affairs will be located.
"Main screen": a card with a greeting of the user and an indication of the number of tasks for the day, a card with progress on completed tasks, a card with all tasks.

![image](https://github.com/xemura/todo-list_app/assets/92382028/111241ce-2fc7-43fd-92fb-391dfe7c5054)

If the user clicks on the very first card, he will go to the fragment with the profile, where he can change the name, log out of the account or return to the to-do list.
The user will also be able to change the password by clicking on the corresponding text on the login fragment.
On the fragment of the password change, the user enters his email, then clicks on the button and a link is sent to his email, which he must follow and enter a new password.
Then the user will be able to return to the login fragment and log in to the profile with new data.

![image](https://github.com/xemura/todo-list_app/assets/92382028/9966d208-33ec-4225-b1a6-677c7183a607)

After closing the application, if the user has not logged out of the account, he will be on the main fragment with his data, and if he left, then on the introductory fragment.

