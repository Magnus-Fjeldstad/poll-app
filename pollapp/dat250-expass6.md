# DAT250: Software Technology Experiment Assignment 3

## Short Report

### API Redesign

First i would like to say that i have decided to start over from scratch
with the API. I though that from my previous experience with openapi generate
i could just do that now as well. But i soon realized that i was having more touble
then I benefitted so i decided to start over from scratch.

Link to previous API:
[Previous API](<https://github.com/Magnus-Fjeldstad/DAT250>)
---

### API Backend

With that out of the box, we can start having a look at my new API
I still used some techniques I learned from my internship like using
Lombok and controller, service, repo pattern. I like this pattern because
it seperates the code nicely and makes it easier to read and understand.
I've had some challenges with
the [PollManager](https://github.com/Magnus-Fjeldstad/poll-app/blob/119ff7dbdf7ec234364656e7ee616ba071da7ac6/pollapp/src/main/java/com/poll/pollapp/manager/PollManager.java)
class. Since im used to writing API'S with a db and not with a in memory db.
I still have som issues with casting a vote, but we will come back to that later.

### Frontend Implementation

Frontend implementation is done with Vue.js since i am a bit familar with it.
It went quite smooth, since I did not have to worry about safety and the backend was already working(ish).
I'm also used to using tailwind for css, but in this project I just used vanilla css.
I also made a file
called [api.js](https://github.com/Magnus-Fjeldstad/poll-app/blob/1638bfb5f40e0b8378539cab6192d25f0a776079/poll-app-frontend/src/api.js)
[Frontend code](https://github.com/Magnus-Fjeldstad/poll-app/blob/1638bfb5f40e0b8378539cab6192d25f0a776079/poll-app-frontend/src)
to handle the requests to the backend in a more structured way. This helped me a lot with the requests.
---

### Technical Challenges

The main challenge i faced working on this API, was trying to get the open api generate to work.
I sat many hours thinking it would save me time, but in the end i had to scrap the entire API and start over from
scratch.
When i finally got to my senses and started coding the API from scratch, it went much smoother.

I also had issues with the PollManager class. Since im used to writing API'S with a db and not with a in memory db.
Working with hashmaps in that manner was a bit challenging, not because it was hard just because i was not used to it.

---

### Pending Issues

I have a ongoing issue with my API that i cant downvote an option. I have some ideas on how to fix it
but i have not had the time to implement it yet. I will probably do some more refactoring to my current desing
espescially the PollManager and how i handle the Poll and Voting, because now it is a bit buggy.



