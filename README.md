# Fitra

## Navigation
- [Description](#Description)
- [Interface Preview](#Interface-Preview)
- [Technologies](#Technologies)
- [How to run](#How-to-run)

# Description
## Implemented features

- Authentication and registration with email confirmation
- Travel management (creating/editing/deleting)
- Travel mates management
    - Invitation sending to the travel and its management (viewing/approving/cancelling)
    - Join request sending to the travel and its management (viewing/approving/cancelling)
- Chat system
- Possibility to leave feedback with a rating in the travel mate's profile

# Interface Preview

![image](https://github.com/stempz101/fitra/assets/59826158/1dc75e27-2375-4b8e-b871-5830d722cab6)
![image](https://github.com/stempz101/fitra/assets/59826158/01785a7b-f032-453f-aec5-ec3acbf826b0)

# Technologies
- Java 17
- Spring (Boot, Web, Data, Security, Validation, Mail Sender, WebSocket)
- Liquibase
- PostgreSQL
- React.js
- Docker

# How to run
Using Docker, just run the next command:
   ```bash
   docker compose up -d
   ```
After starting the application there is already a user with the ADMIN role:
email | password 
--- | --- 
admin@domain.com | admin 
