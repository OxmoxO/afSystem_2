package antifraud.service;

import antifraud.dao.StolenCreditCardDao;
import antifraud.dao.SuspiciousIpDao;
import antifraud.dao.UserDao;
import antifraud.model.entity.StolenCreditCard;
import antifraud.model.entity.SuspiciousIp;
import antifraud.model.entity.User;
import antifraud.model.enums.Role;
import antifraud.security.exeption.CardIsStolenException;
import antifraud.security.exeption.CardNotFoundException;
import antifraud.security.exeption.IpBannedException;
import antifraud.security.exeption.IpNotFoundException;
import antifraud.security.exeption.UserExistsException;
import antifraud.security.exeption.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardAndIpService {

    private final UserDao userDao;
    private final SuspiciousIpDao ipDao;
    private final StolenCreditCardDao cardDao;

    @Autowired

    public CardAndIpService(UserDao userDao, SuspiciousIpDao ipDao, StolenCreditCardDao cardDao) {
            this.userDao = userDao;
            this.ipDao = ipDao;
            this.cardDao = cardDao;
        }

        public User create(User user) {
            if (userDao
                    .existsByUsernameIgnoreCase(user.getUsername())) {
                throw new UserExistsException();
            }
            user.setRole(countUsers() == 0 ?
                    Role.ADMINISTRATOR : Role.MERCHANT);
            user.setLocked(user.getRole() != Role.ADMINISTRATOR);
            return userDao.save(user);
        }

        public List<User> getAllUsers() {
            return (List<User>) userDao.findAll();
        }

        public void delete(String username) {
            userDao.delete(findUser(username));
        }
        public long countUsers() {
            return userDao.count();
        }
        public User findUser(String username) {
            return userDao
                    .findByUsernameIgnoreCase(username)
                    .orElseThrow(UserNotFound::new);
        }
        public void update(User user) {
            userDao.save(user);

    }

    public SuspiciousIp create(SuspiciousIp ip) {
        if (isIpBanned(ip.getIp())) {
            throw new IpBannedException();
        }
        return ipDao.save(ip);
    }

    public boolean isIpBanned(String ip) {
        return ipDao.existsByIp(ip);
    }

    public void deleteSuspiciousIp(String ip) {
        if (!isIpBanned(ip)) {
            throw new IpNotFoundException();
        }
        ipDao.delete(ipDao.findFirstByIp(ip));
    }

    public List<SuspiciousIp> getAllSuspiciousIps() {
        return (List<SuspiciousIp>) ipDao.findAll();
    }

    public StolenCreditCard create(StolenCreditCard card) {
        if (isCardStolen(card.getNumber())) {
            throw new CardIsStolenException();
        }
        return cardDao.save(card);
    }

    public boolean isCardStolen(String number) {
        return cardDao.existsByNumber(number);
    }

    public void deleteStolenCreditCard(String cardNumber) {
        if (!isCardStolen(cardNumber)) {
            throw new CardNotFoundException();
        }
        cardDao.delete(cardDao.findFirstByNumber(cardNumber));
    }

    public List<StolenCreditCard> getAllStolenCards() {
        return (List<StolenCreditCard>) cardDao.findAll();
    }
}
