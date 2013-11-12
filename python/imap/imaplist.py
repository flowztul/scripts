import imaplib
import ssl
import socket
import imapconfig
import re

username = imapconfig.username
password = imapconfig.password
host = imapconfig.host
port = imapconfig.port
ca_certs = imapconfig.ca_certs


class PlainAuthObject(object):

    def __init__(self, user, password):
        self.username = username
        self.password = password

    def callback(self, dummy):
        return '\0%s\0%s' % (self.username, self.password)


class IMAP4_SSL_VALIDATE(imaplib.IMAP4_SSL):

    # Certificate validating IMAP4_SSL class, based on
    # http://stackoverflow.com/questions/9713055/certificate-authority-for-imaplib-and-poplib-python

    def __init__(self, host='', port=imaplib.IMAP4_SSL_PORT, keyfile=None,
                 certfile=None, ca_certs=None):
        self.ca_certs = ca_certs
        imaplib.IMAP4_SSL.__init__(self, host, port, keyfile, certfile)

    def open(self, host='', port=993, ca_certs=None):
        self.host = host
        self.port = port
        self.sock = socket.create_connection((host, port))
        self.sslobj = ssl.wrap_socket(self.sock, self.keyfile, self.certfile,
                                      cert_reqs=ssl.CERT_REQUIRED,
                                      ca_certs=self.ca_certs,
                                      ssl_version=ssl.PROTOCOL_TLSv1)
        self.file = self.sslobj.makefile('rb')

imap = IMAP4_SSL_VALIDATE(host, port, ca_certs=ca_certs)
imap.authenticate('PLAIN', PlainAuthObject(username, password).callback)
response = imap.list()
if response[0] == 'OK':
    mailboxes = response[1]
    matcher = re.compile(r'\(([^)]*)\)\s*"([^"]*)"\s*"([^"]*)"')
    for mailbox in mailboxes:
        matches = matcher.match(mailbox)
        print '+%s' % matches.group(3)
