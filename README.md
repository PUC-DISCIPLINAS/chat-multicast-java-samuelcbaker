#Documentação

##Cliente conectar no servidor:
sudo java redes.UDPClient localhost

##Ligar servidor:
sudo java redes.UDPServer

##Comandos
* CRIAR SALA
     * create_room:{nome_sala}
     * Retorno: id da sala
* LISTAR SALAS
     * list_rooms
     * Retorno: nomes das salas com seus respectivos ids
* ENTRAR NA SALA
     * join_room:{id_sala}:{nome_usuario}
     * Retorno: ip multicast
* LISTAR MEMBROS DA SALA
     * list_members
     * Retorno: membros
* SAIR DA SALA
     * leave_room
     * Retorno: mensagem de sucesso
* ENVIAR MENSAGEM
     * send_message:{mensagem}