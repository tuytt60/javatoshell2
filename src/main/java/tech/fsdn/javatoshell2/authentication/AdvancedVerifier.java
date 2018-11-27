package tech.fsdn.javatoshell2.authentication;

import ch.ethz.ssh2.KnownHosts;
import ch.ethz.ssh2.ServerHostKeyVerifier;

/**
 * @author qi.liu
 * @create 2018-11-27 10:18
 * @desc 描述:
 **/
public class AdvancedVerifier implements ServerHostKeyVerifier {

    KnownHosts database;

    public AdvancedVerifier(KnownHosts database) {
        this.database = database;
    }

    @Override
    public boolean verifyServerHostKey(String hostname, int port, String serverHostKeyAlgorithm,
                                       byte[] serverHostKey) throws Exception {
        final String host = hostname;
        final String algo = serverHostKeyAlgorithm;

        String message;
        int result = database.verifyHostkey(hostname, serverHostKeyAlgorithm, serverHostKey);
        switch (result) {
            case KnownHosts.HOSTKEY_IS_OK:
                return true;
            case KnownHosts.HOSTKEY_IS_NEW:
                message = "Do you want to accept the hostkey (type " + algo + ") from " + host + " ?\n";
                break;
            case KnownHosts.HOSTKEY_HAS_CHANGED:
                message = "WARNING! Hostkey for " + host + " has changed!\nAccept anyway?\n";
                break;
            default:
                throw new IllegalStateException();
        }

        String hexFingerprint = KnownHosts.createHexFingerprint(serverHostKeyAlgorithm, serverHostKey);
        String bubblebabbleFingerprint = KnownHosts.createBubblebabbleFingerprint(serverHostKeyAlgorithm,
                serverHostKey);

        message += "Hex Fingerprint: " + hexFingerprint + "\nBubblebabble Fingerprint: " + bubblebabbleFingerprint;

        /* Be really paranoid. We use a hashed hostname entry */

        String hashedHostname = KnownHosts.createHashedHostname(hostname);
        /* Add the hostkey to the in-memory database */
        database.addHostkey(new String[]{hashedHostname}, serverHostKeyAlgorithm, serverHostKey);
        /* Also try to add the key to a known_host file */
        return true;

    }
}

