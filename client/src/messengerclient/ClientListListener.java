/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;

/**
 *
 * @author  bhaskar
 */
public interface ClientListListener
{
    void addToList(String userName,String ola);
    void removeFromList(String userName);
}
