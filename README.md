# Blockchain-transfer

## Overall decisions

### Behaviours

I've decided to use Scala and Akka to build the Rest API using actors system and picked the suggested bouncycastle
library for cryptography needs.
This API has a single endpoint which attempts to apply a given transaction, so once that transaction is properly signed
and the source has sufficient funds for transfering that currency then the transaction is applied to the central ledger
and that transaction joins to the blocks present in this service node.

### Model consideration

Due to the fact of this project requires a single node application, I didn't concern about using the collections
immutable, but that would be a point to reconsider if it's an application with more concurrent accesses.
Regarding the blocks, I prefer to use a queue to represent the collections of blocks in a blockchain, so I can access
the last one with a constant complexity of O(1) and appending more transactions on that.

### Testing approach

I've decided to create unit test for the BlockChain class, so I can give it a bit more of different options and has
provided the API test with the main scenarios (Valid transaction, Signed by the wrong wallet, Overdraft)

### How to run the application at localhost:8080

`sbt run`

### How to test

`sbt test`