# connect-4
Connect4 game made with java/javaFX. This repo is meant to showcase this small project and not intended to improve or solicit pull requests. I built this around 2024 and I am excited to see how much my programming and development skills have improved when I look back on this repo in the far future :).

The connect4 game has a client server architecture so that two players can play from different computers. The server can handle as many games as the server's host computer has resources for, this is accomplished through using the runnable class for multithreading capabilities. The option to play against an AI is also available to the player, the "AI" is programmed to always move to try and make or make progress on a four in a row win. Due to this, it will not try and block the player's moves intentionally so it can be pretty easy to beat. Finally there is also an option for the player to play either in a GUI or a text-based(perfect for the terminal user) environment. 

Below are some screenshots from the running program, thanks for reading!

**Running Game: Text UI, Player Vs AI**
![image](https://github.com/user-attachments/assets/bbcfb1bc-91cf-4190-873a-5b7e5c051fe0)


**GUI Opening**

![image](https://github.com/user-attachments/assets/b867146f-d388-422c-9f27-4270bd125ef6)

**Running Game: GUI, Player Vs AI**

![image](https://github.com/user-attachments/assets/7df77e7a-22c2-4890-80e7-bc01a07f590d)


**Running Games: GUI, Server + multiple Clients, PVP**
![image](https://github.com/user-attachments/assets/2db61997-371f-4ee1-a6ea-060abb924741)
