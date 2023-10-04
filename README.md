# MediCare
MediCare is a drug selling service, made using Spring Boot and Java.

## DevOps Configuration
### AWS Jenkins Configuration (CI/CD)
To see the configuration of the Jenkins instance [Go here](CI_CD_Server.md)

## Old-fashioned deployment
### Setting Up the Server
- Make sure Java 17 is installed
- Create the `medicare.service` file in `/etc/systemd/system/` and add the `medicare.service` file.
- Run: `systemd enable medicare` to enable the service
- Create an automatic deployment user:
```bash
useradd -m -d /opt/medicare/
```
- Add the `deployer` user into the `sudoers` permissions of the `medicare` service:
```bash
%deployer ALL= NOPASSWD: /bin/systemctl start medicare
%deployer ALL= NOPASSWD: /bin/systemctl stop medicare
%deployer ALL= NOPASSWD: /bin/systemctl restart medicare
```
- Copy the content of `env.example` into `/opt/medicare/.env`