#include<netdb.h>
#include<arpa/inet.h>
#include<sys/time.h>
#include<sys/socket.h>
#include<netinet/in.h>

#include<stdio.h>
#include<unistd.h>

#include<signal.h>

#define MAX_REP 5
#define ADR2  "158.196.246.18"
#define DADDR "158.196.157.210"

int repeat=0;
int sock;
char *data="ABCD1234";
char data2[20000];
struct sockaddr_in adr;

void alarmed(int signo)
{
  if (repeat++ > MAX_REP) { 
    signal(SIGALRM, NULL); 
    alarm(0);
    printf("failed\n");
    exit(1);
  } else {
    printf("retransmit\n");
    signal(SIGALRM, alarmed);
//    adr.sin_addr.s_addr=inet_addr(ADR2);
    sendto(sock, data, strlen(data), 0, (struct sockaddr*) &adr, sizeof(adr)); 
    alarm(1);
  }
}

int main(void)
{
  int len;
  adr.sin_family = AF_INET;
  adr.sin_addr.s_addr = INADDR_ANY;
  adr.sin_port = htons(31217);
  sock=socket(PF_INET, SOCK_DGRAM, 0);
  bind(sock, (struct sockaddr*) &adr, sizeof(adr));
  adr.sin_addr.s_addr=inet_addr(DADDR);
  sendto(sock, data, strlen(data), 0, (struct sockaddr*) &adr, sizeof(adr)); 
  printf("sent\n");
  alarm(1);
  signal(SIGALRM, alarmed);
  len=sizeof(struct sockaddr);
  recvfrom(sock, data2, strlen(data), 0, (struct sockaddr*) &adr, &len); 
  printf("received\n");
  alarm(0);
  repeat=0;
}
