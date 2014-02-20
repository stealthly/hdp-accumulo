hdp-accumulo
============

Virtual development environment with Hortonworks Data Platform and Apache Accumulo running 

Use Vagrant to get up and running.

1) Install Vagrant [http://www.vagrantup.com/](http://www.vagrantup.com/)  
2) Install Virtual Box [https://www.virtualbox.org/](https://www.virtualbox.org/)  

    git clone https://github.com/stealthly/hdp-accumulo.git
    cd hdp-accumulo
    vagrant up

Now you can access Accumulo from your code on your local machine  
zk=172.16.25.10  
instancename=dev  
username=root  
password=dev  

  
To acess the Accumulo shell (password == dev) you can login to the virtual machine
    vagrant ssh
    sudo /usr/lib/accumulo/bin/accumulo shell -u root



