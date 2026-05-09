import { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// pasamos el id del usuario con el que 
// vamos a chatear
const Chat = ({ receptorId })