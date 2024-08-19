# Blockchain-transfer

## Overall decisions

### Behaviours

I've decided to use Scala and Akka to build the Rest API using actors system and picked the suggested bouncycastle
library for cryptography needs.
This API has a single endpoint which attempts to apply a given transaction, so once that transaction is properly signed
and the source has sufficient funds for transfering that currency then the transaction is applied to the central ledger
and that transaction joins to the blocks present in this service node.

### Testing approach

I've decided to create unit test for the BlockChain class, so I can give it a bit more of different options and has
provided the API test with the main scenarios (Valid transaction, Signed by the wrong wallet, Overdraft)

### How to run the application at localhost:8080

`sbt run`

### How to test

`sbt test`