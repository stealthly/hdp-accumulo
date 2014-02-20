hdp-accumulo
============

Virtual development environment with Hortonworks Data Platform and Apache Accumulo running 

Use Vagrant to get up and running.

1) Install Vagrant [http://www.vagrantup.com/](http://www.vagrantup.com/)  
2) Install Virtual Box [https://www.virtualbox.org/](https://www.virtualbox.org/)  

    git clone https://github.com/stealthly/hdp-accumulo.git
    cd hdp-accumulo
    vagrant up



To acess the Accumulo shell (password == dev)
    vagrant ssh
    sudo /usr/lib/accumulo/bin/accumulo shell -u root

