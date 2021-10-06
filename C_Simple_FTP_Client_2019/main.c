/* header files */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <netdb.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <sys/fcntl.h>
#include <arpa/inet.h>

/* define macros*/
#define MAXBUF	1024
#define STDIN_FILENO	0
#define STDOUT_FILENO	1

/* define FTP reply code */
#define USERNAME	220
#define PASSWORD	331
#define LOGIN		230
#define PATHNAME	257
#define CLOSEDATA	226
#define ACTIONOK	250
#define FAIL 550
#define IF_NAME "eth0"
//#define SUCCESS 200

/* DefinE global variables */
char	*host;		/* hostname or dotted-decimal string */
int 	port;
int ascii;
int mode; //1 for active mode, 0 for passive mode;
int mysockfd;
char	*rbuf, *rbuf1;		/* pointer that is malloc'ed */
char	*wbuf, *wbuf1;		/* pointer that is malloc'ed */
struct sockaddr_in	servaddr; //internet-specific socket address
struct hostent *ht;

int	cliopen(char *host, int port);
int	strtosrv(char *str, char *host);
void	cmd_tcp(int sockfd);
void	ftp_list(int sockfd);
void	ftp_get(int sck, char *filename);
void	ftp_put(int sck, char *filename);
void toWindows(char* str );
void toLinux(char* str);
int getAddr(char * str);


int
main(int argc, char *argv[])
{
	int	fd;

	if (0 != argc-2)
	{
		printf("%s\n","missing <hostname>");
		exit(0);
	}

	host = argv[1];
	port = 21;
	ascii =0;

	/*****************************************************************
	//1. code here: Allocate the read and write buffers before open().
	*****************************************************************/
	rbuf = (char*)malloc(MAXBUF * sizeof(char)); //¶¯Ì¬ÄÚ´æ·ÖÅä
	rbuf1 = (char*)malloc(MAXBUF * sizeof(char));
	wbuf = (char*)malloc(MAXBUF * sizeof(char));
	wbuf1 = (char*)malloc(MAXBUF * sizeof(char));

	fd = cliopen(host, port);

	cmd_tcp(fd);

	exit(0);
}


/* Establish a TCP connection from client to server */
int
cliopen(char *host, int port)
{
	/*************************************************************
	//2. code here 
	*************************************************************/
	int control_sock;
    ht = NULL;
    control_sock = socket(AF_INET,SOCK_STREAM,0);  // creat a new socket 
    if(control_sock < 0)
    {
       printf("socket error\n");
       return -1;
    }
    ht = gethostbyname(host);  //·µ»Ø¶ÔÓ¦ÓÚ¸ø¶¨Ö÷»úÃûµÄ°üº¬Ö÷»úÃû×ÖºÍµØÖ·ÐÅÏ¢µÄhostent½á¹¹µÄÖ¸Õë£¬Ê§°Ü·µ»Ø¿ÕÖ¸Õë 
    if(!ht) //gethost Ê§°Ü 
    { 
        return -1;
    }
    //memcpy(&servaddr.sin_addr.s_addr,ht->h_addr,ht->h_length);
   
    memset(&servaddr,0,sizeof(struct sockaddr_in)); //Çå¿Õ½á¹¹Ìå 
    //memcpy:´ÓÔ´ÄÚ´æµØÖ·µÄÆðÊ¼Î»ÖÃ¿ªÊ¼¿½±´Èô¸É¸ö×Ö½Úµ½Ä¿±êÄÚ´æµØÖ·ÖÐ  void *memcpy(void *dest, const void *src, size_t n)
    memcpy(&servaddr.sin_addr.s_addr,ht->h_addr,ht->h_length);  //¸øsockt addr µÄsin_addr.s_addr¸³Öµ 
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(port); //×Ö½ÚÐò×ª»» 
    
    if(connect(control_sock,(struct sockaddr*)&servaddr,sizeof(struct sockaddr)) == -1) 
    {
        return -1;
    }
    return control_sock; //socket to control

}

int actopen(){
	int sock_fd;

    struct sockaddr_in mysockaddr;
    struct sockaddr_in sockAddr;
    socklen_t sockAddr_size;

    mysockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (mysockfd == -1)
        printf("sock() error");
    printf("132\n");

    memset(&mysockaddr, 0, sizeof(mysockaddr));
    printf("135\n");
    mysockaddr.sin_family = AF_INET;
    printf("137\n");
    //memcpy(&mysockaddr.sin_addr.s_addr,ht->h_addr,ht->h_length);
    mysockaddr.sin_addr.s_addr = htonl(INADDR_ANY); //here mistake
    printf("138");
    mysockaddr.sin_port = htons(50724);
    printf("140");

    if (bind(mysockfd, (struct sockaddr*)&mysockaddr, sizeof(mysockaddr)) == -1)
        printf("bind() error");

    if (listen(mysockfd, 5) == -1)
        printf("listen() error");

    sockAddr_size = sizeof(sockAddr);
    accept(mysockfd, (struct sockaddr*)&sockAddr, &sockAddr_size);
    if (sock_fd == -1)
        printf("accept() error");

    return sock_fd;
}
/*
   Compute server's port by a pair of integers and store it in char *port
   Get server's IP address and store it in char *host
*/
int
strtosrv(char *str, char *host)
{
	/*************************************************************
	//3. code here
	*************************************************************/
	int addr[6];
   //printf("%s\n",str);
   ////sscanfÒÔ¹Ì¶¨×Ö·û´®ÎªÊäÈëÔ´   FTP·µ»ØµÄÊ±ºòÊÇÒÔ£¨¡­£¬¡­£¬¡­£¬¡­£¬¡­£¬¡­£© ÐÎÊ½·µ»ØµÄ£¬×¢ÒâÓÐÀ¨ºÅ 
   sscanf(str,"%*[^(](%d,%d,%d,%d,%d,%d)",&addr[0],&addr[1],&addr[2],&addr[3],&addr[4],&addr[5]); //%[^a] È¡µ½Ö¸¶¨×Ö·ûÎªÖ¹µÄ×Ö·û´®£»%* Æ¥Åäµ½µÄ±»¹ýÂËµô 
   bzero(host,strlen(host)); //ÖÃ×Ö½Ú×Ö·û´®Ç°n¸ö×Ö½ÚÎªÁãÇÒ°üÀ¨¡®\0¡¯ 
   sprintf(host,"%d.%d.%d.%d",addr[0],addr[1],addr[2],addr[3]);
   port = addr[4]*256 + addr[5]; 
   return port;
}


/* Read and write as command connection */
void
cmd_tcp(int sockfd)
{
	int		maxfdp1, nread, nwrite, data_con, replycode,tag;
	char		host[16];
	int			port;
	fd_set		rset;

	FD_ZERO(&rset);
	maxfdp1 = sockfd + 1;	/* check descriptors [0..sockfd] */

	for ( ; ; )
	{
		FD_SET(STDIN_FILENO, &rset); //°ÑS¡­¡­£¨±ê×¼ÊäÈëÉè±¸£©£¬Ìí¼Óµ½ÁËÒª¼à¿ØµÄ¼¯ºÏÀï
		FD_SET(sockfd, &rset); //°ÑserverµÄsocket fd,Ìí¼Óµ½ÁË¡­¡­ 

		if (select(maxfdp1, &rset, NULL, NULL, NULL) < 0)
			printf("select error\n");
			
		/* data to read on stdin ÓÃ»§ÊäÈë*/
		if (FD_ISSET(STDIN_FILENO, &rset)) {
			
			//read()»á°Ñ²ÎÊýfdËùÖ¸µÄÎÄ¼þ´«ËÍnbyte¸ö×Ö½Úµ½bufÖ¸ÕëËùÖ¸µÄÄÚ´æÖÐ£¬³É¹¦ Ôò·µ»Ø¶ÁÈ¡µÄ×Ö½ÚÊý 
			if ( (nread = read(STDIN_FILENO, rbuf, MAXBUF)) < 0) 
				printf("read error from stdin\n");
			nwrite = nread+5; 

			/* send username */
			if (replycode == USERNAME) {
				sprintf(wbuf, "USER %s", rbuf);//sprintf()°Ñ¸ñÊ½»¯µÄÊý¾ÝÐ´ÈëÄ³¸ö×Ö·û´®ÖÐ
				if (write(sockfd, wbuf, nwrite) != nwrite)
					printf("write error\n");
				system("stty -echo"); // set anonymous
			}

			 /*************************************************************
			 //4. code here: send password
			 *************************************************************/
			 if(replycode == PASSWORD)
              {
                   sprintf(wbuf,"PASS %s",rbuf);
                   if(write(sockfd,wbuf,nwrite) != nwrite)
                      printf("write error\n");
                  system("stty echo");
              }

			 /* send command */
			if (replycode==LOGIN || replycode==CLOSEDATA || replycode==PATHNAME || replycode==ACTIONOK || replycode == FAIL)
			{
				/* ls - list files and directories*/
				if (strncmp(rbuf, "ls", 2) == 0) {
					tag = 1;
					if(mode==1){
						getAddr(wbuf);
						write(sockfd, wbuf, strlen(wbuf));
					}
					else{
						sprintf(wbuf, "%s", "PASV\n"); //±»¶¯Ä£Ê½
						write(sockfd, wbuf, strlen(wbuf));
					}
					sprintf(wbuf1, "%s", "LIST\n");
					nwrite = 5;
					continue;
				}

				/*************************************************************
				// 5. code here: cd - change working directory/
				*************************************************************/
				if(strncmp(rbuf,"cd",2) == 0)
                 {
                     //sprintf(wbuf,"%s","PASV\n");
					 sscanf(rbuf, "%*s%s", rbuf1);
					 sprintf(wbuf, "CWD %s\n", rbuf1);
                     write(sockfd,wbuf,strlen(wbuf));
                     //sprintf(wbuf1,"%s","CWD\n");
                     
                     continue;
                 }

				/* pwd -  print working directory */
				if (strncmp(rbuf, "pwd", 3) == 0) {
					sprintf(wbuf, "%s", "PWD\n");
					write(sockfd, wbuf, strlen(wbuf));
					continue;
				}

				/*************************************************************
				// 6. code here: quit - quit from ftp server
				*************************************************************/
				  if(strncmp(rbuf,"quit",4) == 0)
                 {
                     sprintf(wbuf,"%s","QUIT\n");
                     write(sockfd,wbuf,strlen(wbuf));
                    if(close(sockfd) <0)
                       printf("close error\n");
                    break;
                 }

				/*************************************************************
				// 7. code here: get - get file from ftp server
				*************************************************************/
				if(strncmp(rbuf,"get",3) == 0)
                 {
                     tag = 2;
                     if(mode==1){
						getAddr(wbuf);
						write(sockfd, wbuf, strlen(wbuf));
					}
					else{
                     	sprintf(wbuf,"%s","PASV\n");   
					 	write(sockfd, wbuf, strlen(wbuf));
         			}
					 sscanf(rbuf, "%*s%s", rbuf1);
					 sprintf(wbuf1, "RETR %s\n", rbuf1);
					 nwrite = nread + 2;
                     continue;
                 }

				/*************************************************************
				// 8. code here: put -  put file upto ftp server
				*************************************************************/
				if(strncmp(rbuf,"put",3) == 0)
                 {
                     tag = 3;
                     if(mode==1){
						getAddr(wbuf);
						write(sockfd, wbuf, strlen(wbuf));
					}
					else{
                     	sprintf(wbuf,"%s","PASV\n");
                     	write(sockfd,wbuf,strlen(wbuf));
	                }
					 sscanf(rbuf, "%*s%s", rbuf1);
					 sprintf(wbuf1, "STOR %s\n", rbuf1);
					 nwrite = nread + 2;
                     continue;
                 }

				/*delete a file*/
				if (strncmp(rbuf, "delete", 2) == 0)
				{
					sscanf(rbuf, "%*s%s", rbuf1);
					sprintf(wbuf, "DELE %s\n", rbuf1);
					write(sockfd, wbuf, strlen(wbuf));
					continue;
				}

				if(strncmp(rbuf, "rename", 6) == 0){
					sscanf(rbuf, "%*s%s%s", rbuf1,wbuf1);
					//write(STDOUT_FILENO, wbuf1, strlen(wbuf1));
					sprintf(wbuf, "RNFR %s\n", rbuf1);
					write(sockfd, wbuf, strlen(wbuf));
				}

				if(strncmp(rbuf, "mkdir", 5) == 0)
				{
					sscanf(rbuf, "%*s%s", rbuf1);
					sprintf(wbuf, "MKD %s\n", rbuf1);
					write(sockfd, wbuf, strlen(wbuf));
					continue;
				}

				if (strncmp(rbuf, "ascii", 5) == 0)
				{
					ascii = 1;
					sprintf(wbuf, "%s", "TYPE A\n");
					write(sockfd, wbuf, strlen(wbuf));

					continue;
				}

				if (strncmp(rbuf, "binary", 5) == 0)
				{
					ascii = 0;
					sprintf(wbuf, "%s", "TYPE I\n");
					write(sockfd, wbuf, strlen(wbuf));

					continue;
				}
				if(strncmp(rbuf,"passive",7) == 0){
					if(mode == 0){
						mode =1;
						printf("Passive mode is off..\n");
					}
					else{
						mode =0;
						printf("Passive mode is on..\n");
					}
				}

			}
		}

		/* data to read from socket */
		if (FD_ISSET(sockfd, &rset)) {
			if ( (nread = recv(sockfd, rbuf, MAXBUF, 0)) < 0) // reply code ·ÅÈërbufÖÐ£¬nread ´æreplay code µÄ³¤¶È
				printf("recv error\n");
			else if (nread == 0)
				break;

			/* set replycode and wait for user's input */
			if (strncmp(rbuf, "220", 3)==0 || strncmp(rbuf, "530", 3)==0){
				strcat(rbuf,  "your name: ");
				nread += 12;
				replycode = USERNAME;
			}

			/*************************************************************
			// 9. code here: handle other response coming from server
			*************************************************************/
			 if(strncmp(rbuf,"331",3) == 0)
             {
                strcat(rbuf,"password: ");
                nread += 11;
                replycode = PASSWORD;
             }
             if(strncmp(rbuf,"230",3) == 0)
             {
                /*if(write(STDOUT_FILENO,rbuf,nread) != nread)
                    printf("write error to stdout\n");*/
                replycode = LOGIN;
             }
             if(strncmp(rbuf,"257",3) == 0)
             {
                /*if(write(STDOUT_FILENO,rbuf,nread) != nread)
                    printf("write error to stdout\n");*/
                replycode = PATHNAME;  
             }
             if(strncmp(rbuf,"226",3) == 0)
             {
                /*if(write(STDOUT_FILENO,rbuf,nread) != nread)
                    printf("write error to stdout\n");*/
                replycode = CLOSEDATA;
             }
             if(strncmp(rbuf,"250",3) == 0)
             {
                /*if(write(STDOUT_FILENO,rbuf,nread) != nread)
                    printf("write error to stdout\n");*/
                replycode = ACTIONOK;
             }
             if(strncmp(rbuf,"550",3) == 0)
             {
				 replycode = FAIL;
             }

			 if(strncmp(rbuf,"350",3) == 0){

			 	char* str = "receive 350";
			 	write(STDOUT_FILENO, str, strlen(str));
			 	char *buf;
				buf = (char*)malloc(MAXBUF * sizeof(char));
			 	sprintf(buf, "RNTO %s\n", wbuf1);
			 	//write(STDOUT_FILENO, buf, strlen(buf));
			 	write(sockfd, buf, strlen(buf));
			 }
 
			 if (strncmp(rbuf, "227", 3) == 0)
			 {
				 //printf("%d\n",1);
				 /*if(write(STDOUT_FILENO,rbuf,nread) != nread)
					printf("write error to stdout\n");*/

				 port = strtosrv(rbuf, host);
				 data_con = cliopen(host, port);
				 write(sockfd, wbuf1, nwrite);

				 if (tag == 1)
				 {
					 ftp_list(data_con);

				 }
				 if (tag == 2)
				 {
					 printf("%s\n",rbuf1);
					 ftp_get(data_con, rbuf1);
				 }

				 if (tag == 3)
				 {
					ftp_put(data_con, rbuf1);
				 }
				 nwrite = 0;
			 }
			 if (strncmp(rbuf, "200", 3) == 0)
			 {
				 data_con = actopen();
				 write(sockfd, wbuf1, nwrite);

				 if (tag == 1)
				 {
				 	printf("tag == 1");
					 ftp_list(data_con);

				 }
				 if (tag == 2)
				 {
					 printf("%s\n",rbuf1);
					 ftp_get(data_con, rbuf1);
				 }

				 if (tag == 3)
				 {
					ftp_put(data_con, rbuf1);
				 }
				 nwrite = 0;
			 }
			 
			/* start data transfer */
			if (write(STDOUT_FILENO, rbuf, nread) != nread)
				printf("write error to stdout\n");
		}
	}


	if (close(sockfd) < 0)
		printf("close error\n");
	if(mode == 1){
		if(close(mysockfd))
			printf("sock_fd close error\n");
	}
}


/* Read and write as data transfer connection */
void
ftp_list(int sockfd)
{
	int		nread;

	for ( ; ; )
	{
		/* data to read from socket */
		if ( (nread = recv(sockfd, rbuf1, MAXBUF, 0)) < 0)
			printf("recv error\n");

		if (write(STDOUT_FILENO, rbuf1, nread) != nread)
			printf("send error to stdout\n");
		if (nread == 0)
			break;
	}

	if (close(sockfd) < 0)
		printf("close error\n");
}

/* download file from ftp server */
void	
ftp_get(int sck, char *filename)
{
	/*************************************************************
	// 10. code here
	*************************************************************/
	int fd=open(filename, O_WRONLY | O_CREAT | O_TRUNC, S_IREAD| S_IWRITE);
	if(fd==-1)
		printf("open file error\n");
	int nread;
	for (;;)
	{
		bzero(rbuf1,MAXBUF);
		if ((nread = recv(sck, rbuf1, MAXBUF, 0)) < 0)
		{
			printf("receive error");
			break;
		}
		if (nread == 0)
		{
			break;
		}
		if (write(STDOUT_FILENO, rbuf1, nread) != nread)
			printf("receive error from server!");

		if(ascii==1){
			toWindows(rbuf1);
		}
		if (write(fd, rbuf1, nread) != nread)
			printf("receive error from server!");

	}
	if (close(sck) < 0)
		printf("close error\n");
}

/* upload file to ftp server */
void
ftp_put(int sck, char *filename)
{
	/*************************************************************
	// 11. code here
	*************************************************************/
	
	int fd;
	char *str;
	char *buf;
	buf = (char*)malloc(MAXBUF * sizeof(char));
	fd=open(filename, O_RDWR);
	if(fd==-1){
		str = "open file error\n";
		write(STDOUT_FILENO, str, strlen(str));
	}
	
	int nread;

	lseek(fd, 0, SEEK_SET);
	nread = read(fd, buf, MAXBUF);

	if (nread < 0)
	{
		str ="read failure.\n";
		write(STDOUT_FILENO, str, strlen(str));
	}
	if(ascii==1){
			toLinux(rbuf1);
	}
	if (write(STDOUT_FILENO, buf, nread) != nread){
		str = "stout send error";
		write(STDOUT_FILENO, str, strlen(str));
	}
	if (write(sck, buf, nread) != nread){
		str = "socket send error";
		write(STDOUT_FILENO, str, strlen(str));
	}	

	if (close(sck) < 0){
		str = "close error";
		write(STDOUT_FILENO, str, strlen(str));
	}

}

void toWindows(char* str ){
	int m = 0;
	int n = strlen(str);
	for(;m<n;m++){
		if((*(str+m))=='\n')
			*(str+m)='\r\n';
	}
} 
void toLinux(char* str ){
	int m = 0;
	int n = strlen(str);
	for(;m<n;m++){
		if((*(str+m))=='\r\n')
			*(str+m)='\n';
	}
}
int getAddr(char * wbuf)
{
    char *temp = NULL;
    int inet_sock;
    struct ifreq ifr;
    bzero(wbuf,MAXBUF);

    inet_sock = socket(AF_INET, SOCK_DGRAM, 0); 

    memset(ifr.ifr_name, 0, sizeof(ifr.ifr_name));
    memcpy(ifr.ifr_name, IF_NAME, strlen(IF_NAME));

    if(0 != ioctl(inet_sock, SIOCGIFADDR, &ifr)) 
    {   
        perror("ioctl error");
        return -1;
    }

    temp = inet_ntoa(((struct sockaddr_in*)&(ifr.ifr_addr))->sin_addr);     
    memcpy(wbuf, temp, strlen(temp));

    close(inet_sock);
    char* str = (char*)malloc(100 * sizeof(char));
    str = strtok(wbuf,".");
	char* token = strtok(NULL,".");
    while(token!=NULL){
		sprintf(str,"%s,%s",str,token);
        token = strtok(NULL,".");
     }
    char s[100] ={0};
    sprintf(s,"%s,198,36",str);//50724
	sprintf(wbuf,"PORT %s\n",s);
	printf("%s",wbuf);

    return 0;
}

/*int s(char* str, char* s2)
{
	//char s1[100];

	return sscanf(str, " get %s", s2) == 1;

} */
