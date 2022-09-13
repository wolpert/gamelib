# Net Server

## Connection Details

![connection](http://www.plantuml.com/plantuml/png/RL5DIyGm4BttLyon2ow8nnwMrLN4i_w18Hbt1zAa9DFY_dipQQ2cU2dqlNaVhpqdmIZpw3eq4Y9S79CNm0IccpB3XYVCm2V5XQBYgS6_U5nXQU3dMMxayojAM5NuZztbXjwJAtmRzuuZANpjpF6K1Z31UpBI7xGcBoYKUwJZIGOe0qgDtZp0qsCwA64oKJ9UIP1ToafBcxRQOgxPizMcSjzx9nnWGSSMXRQZcXff30lLsAnq0lnT_3e05PS2yb5bctwbhxMtxlp3aKkq7cIvvRJD9c_R0INIX_AbuULUVmJzJ1nHEFXEoa9H-4rHihtPJzodD0aQUSwFUNI_)

<!--
```plantuml
@startuml
actor Client as c
participant Server as s
participant Timer as t
participant Authenticator as sa
participant ServerChannel as sc
participant Game as g
c->s: connect()
activate s
s->t: startTimer(c, 20s)
s->c: serverDetails()
deactivate s
c->s: auth(identity)
activate s
s->sa: validate(identity)
s->t: removeTimer(c)
alt if auth
  s->sc: setIdentity(identity)
  s->g: startGame(identity)
else
  s->c: disconnect()
end
deactivate s

alt no Auth By Timer expiration
t->s: timerExpired(c)
activate s
s->c: disconnect()
deactivate s
end
@enduml
```
-->