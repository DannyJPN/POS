#include <fcntl.h>
#include <sys/ioctl.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>

int fd;
 
void setDTR() {
    int iFlags = TIOCM_DTR;
    ioctl(fd, TIOCMBIS, &iFlags);
}

void setRTS() {
    int iFlags = TIOCM_RTS;
    ioctl(fd, TIOCMBIS, &iFlags);
}

void clearDTR() {
    int iFlags = TIOCM_DTR;
    ioctl(fd, TIOCMBIC, &iFlags);
}

void clearRTS() {
    int iFlags = TIOCM_RTS;
    ioctl(fd, TIOCMBIC, &iFlags);
}

int getCTS() {
    int iFlags;
    ioctl(fd, TIOCMGET, &iFlags);
    return (iFlags & TIOCM_CTS);
} 

int getDSR() {
    int iFlags;
    ioctl(fd, TIOCMGET, &iFlags);
    return (iFlags & TIOCM_DSR);
} 

int main(int argc,char *argv[]) {
  if (argc != 2) {
     printf("Chybi povinny parametr port (napr. /dev/ttyS0)\n");
     return(1);
     }

  fd = open(argv[1], O_RDWR || O_NONBLOCK);

  while(1) {
    setDTR();
    clearRTS();
    printf("%d %d\n",getDSR(),getCTS());
    sleep(1);
    clearDTR();
    setRTS();
    printf("%d %d\n",getDSR(), getCTS());
    sleep(1);
  }  
  

}

