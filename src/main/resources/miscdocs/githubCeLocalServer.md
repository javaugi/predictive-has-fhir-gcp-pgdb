#### How to see github UI
When GitLab CE is installed in WSL, you can't access it from your Windows browser at http://localhost. This is because WSL has its own network interface and IP address, 
separate from the Windows host. The localhost on your Windows machine refers to the Windows network, not the network inside your WSL virtualized environment.
To access the GitLab UI, you need to find the specific IP address assigned to your WSL instance and use that in your browser.
Step 1: Find the WSL IP Address (eth0): Open your WSL Ubuntu terminal.
	ip addr show eth0 | grep inet
	inet 172.30.154.157/20 brd 172.30.159.255 scope global eth0
Step 2: Access GitLab from Your Windows Browser: Open your Windows web browser. In the address bar, type the IP address you found in the previous step. For example:
http://172.30.154.157   (admin,root/JiaxianHmeGhce1@8)
################    Restart All GitLab Services: 
The most common way to reboot GitLab is to restart all its services. This is the command you'll use most of the time.
Open your WSL Ubuntu terminal. (enter admin pwd for sudo)
	sudo gitlab-ctl restart: The command will show you the status of each service as it shuts down and then starts up again. It will take a few minutes for everything to be fully operational.
	sudo gitlab-ctl stop: This command stops all GitLab services. This is useful if you need to perform maintenance or are shutting down your computer for an extended period.
	sudo gitlab-ctl start: This command starts all GitLab services if they have been stopped.
	sudo gitlab-ctl reconfigure: This command is not a simple restart, but it is often necessary after a restart or after making changes to the /etc/gitlab/gitlab.rb configuration file. It re-applies all the settings and restarts only the services that need it. It is a more robust way to ensure your configuration is correctly applied.
	sudo gitlab-ctl status: This command checks the status of all GitLab services and shows you whether they are running or not. It's a great way to verify that everything is working as expected after a restart or reconfigure.

