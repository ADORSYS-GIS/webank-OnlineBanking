import { Command as $Command } from "@smithy/smithy-client";
import { MetadataBearer as __MetadataBearer } from "@smithy/types";
import {
  CloudWatchClientResolvedConfig,
  ServiceInputTypes,
  ServiceOutputTypes,
} from "../CloudWatchClient";
import {
  PutManagedInsightRulesInput,
  PutManagedInsightRulesOutput,
} from "../models/models_0";
export { __MetadataBearer };
export { $Command };
export interface PutManagedInsightRulesCommandInput
  extends PutManagedInsightRulesInput {}
export interface PutManagedInsightRulesCommandOutput
  extends PutManagedInsightRulesOutput,
    __MetadataBearer {}
declare const PutManagedInsightRulesCommand_base: {
  new (
    input: PutManagedInsightRulesCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    PutManagedInsightRulesCommandInput,
    PutManagedInsightRulesCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  new (
    input: PutManagedInsightRulesCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    PutManagedInsightRulesCommandInput,
    PutManagedInsightRulesCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  getEndpointParameterInstructions(): import("@smithy/middleware-endpoint").EndpointParameterInstructions;
};
export declare class PutManagedInsightRulesCommand extends PutManagedInsightRulesCommand_base {
  protected static __types: {
    api: {
      input: PutManagedInsightRulesInput;
      output: PutManagedInsightRulesOutput;
    };
    sdk: {
      input: PutManagedInsightRulesCommandInput;
      output: PutManagedInsightRulesCommandOutput;
    };
  };
}
